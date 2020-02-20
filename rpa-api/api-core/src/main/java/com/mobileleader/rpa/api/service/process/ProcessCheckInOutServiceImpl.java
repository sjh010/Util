package com.mobileleader.rpa.api.service.process;

import com.mobileleader.rpa.api.data.mapper.biz.ProcessHistoryMapper;
import com.mobileleader.rpa.api.data.mapper.biz.ProcessMapper;
import com.mobileleader.rpa.api.data.mapper.biz.ProcessVersionMapper;
import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.api.model.request.ProcessCheckInRequest;
import com.mobileleader.rpa.api.model.request.ProcessCheckOutRequest;
import com.mobileleader.rpa.api.model.request.ProcessRegistRequest;
import com.mobileleader.rpa.api.model.response.ProcessCheckInResponse;
import com.mobileleader.rpa.api.model.response.ProcessCheckOutResponse;
import com.mobileleader.rpa.api.model.response.ProcessHistoryInfo;
import com.mobileleader.rpa.api.model.response.ProcessHistoryResponse;
import com.mobileleader.rpa.api.model.response.ProcessInfo;
import com.mobileleader.rpa.api.model.response.ProcessListResponse;
import com.mobileleader.rpa.api.support.AuthenticationTokenSupport;
import com.mobileleader.rpa.api.support.BizLogSupport;
import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.biz.Process;
import com.mobileleader.rpa.data.dto.biz.ProcessHistory;
import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import com.mobileleader.rpa.data.dto.common.CommonFile;
import com.mobileleader.rpa.data.type.ConfigManagementStatusCode;
import com.mobileleader.rpa.data.type.FileGroupColumdId;
import com.mobileleader.rpa.data.type.FileGroupTableId;
import com.mobileleader.rpa.data.type.ProcessHistoryTypeCode;
import com.mobileleader.rpa.repository.file.FileGroupRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProcessCheckInOutServiceImpl implements ProcessCheckInOutService {

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProcessVersionMapper processVersionMapper;

    @Autowired
    private ProcessHistoryMapper processHistoryMapper;

    @Autowired
    private FileGroupRepository fileGroupRepository;

    @Autowired
    private BizLogSupport bizLogSupport;

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.STUDIO_API)
    public ProcessCheckInResponse registProcess(ProcessRegistRequest request) {

        // 프로세스 명 중복 체크
        if (processMapper.countByProcessName(request.getProcessName()) > 0) {
            return ProcessCheckInResponse.newErrorInstance(RpaApiError.DUPLICATE_PROCESS_NAME);
        }

        // 파일 등록
        Integer fileGroupSequence = null;
        try {
            for (MultipartFile multipartFile : request.getProcessFile()) {
                if (fileGroupSequence != null) {
                    fileGroupRepository.addFile(multipartFile, fileGroupSequence,
                            request.getProcessFileRemarksContent());
                } else {
                    CommonFile commonFile = fileGroupRepository.addFile(multipartFile,
                            FileGroupTableId.PROCESS_VERSION_TABLE, FileGroupColumdId.PROCESS_FILE_GROUP_SEQUENCE,
                            request.getProcessFileRemarksContent());
                    fileGroupSequence = commonFile.getFileGroupSequence();
                }
            }
        } catch (IOException e) {
            throw new RpaApiException(RpaApiError.FILE_IO_ERROR);
        }
        // 프로세스 등록
        Integer processSequence = insertProcess(request.getProcessName());
        // 프로세스 버전 등록
        ProcessVersion processVersion =
                insertProcessVersion(processSequence, fileGroupSequence, request.getRemarksContent(), 0, 1);
        // 히스토리 등록
        bizLogSupport.addProcessHistory(processVersion.getProcessVersionSequence(), ProcessHistoryTypeCode.REGIST,
                request.getRemarksContent());
        return new ProcessCheckInResponse.Builder().processSequence(processSequence)
                .version(processVersion.getMajorVersion(), processVersion.getMinorVersion()).build();
    }

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.STUDIO_API)
    public ProcessCheckInResponse checkInProcess(ProcessCheckInRequest request) {
        Process process = processMapper.selectByPrimaryKey(request.getProcessSequence());

        // 체크아웃 프로세스 존재 여부 확인
        if (process == null) {
            return ProcessCheckInResponse.newErrorInstance(RpaApiError.PROCESS_NOT_FOUND);
        }

        // 체크아웃 상태 확인
        if (!ConfigManagementStatusCode.CHECK_OUT.getCode().equals(process.getConfigManagementStatusCode())) {
            return ProcessCheckInResponse.newErrorInstance(RpaApiError.ALREADY_CHECKED_IN_PROCESS,
                    process.getConfigManagementUserId());
        }

        // 형상관리 사용자 확인
        if (!AuthenticationTokenSupport.getUserId().equals(process.getConfigManagementUserId())) {
            return ProcessCheckInResponse.newErrorInstance(RpaApiError.NOT_MATCHED_CHECK_OUT_USER,
                    process.getConfigManagementUserId());
        }

        // 파일 등록
        Integer fileGroupSequence = null;
        try {
            for (MultipartFile multipartFile : request.getProcessFile()) {
                if (fileGroupSequence != null) {
                    fileGroupRepository.addFile(multipartFile, fileGroupSequence,
                            request.getProcessFileRemarksContent());
                } else {
                    CommonFile commonFile = fileGroupRepository.addFile(multipartFile,
                            FileGroupTableId.PROCESS_VERSION_TABLE, FileGroupColumdId.PROCESS_FILE_GROUP_SEQUENCE,
                            request.getProcessFileRemarksContent());
                    fileGroupSequence = commonFile.getFileGroupSequence();
                }
            }
        } catch (IOException e) {
            throw new RpaApiException(RpaApiError.FILE_IO_ERROR);
        }

        ProcessVersion latestProcessVersion =
                processVersionMapper.selectLatestVersionByProcessSequence(process.getProcessSequence());

        // 신규 프로세스 버전 Insert
        ProcessVersion processVersion =
                insertProcessVersion(request.getProcessSequence(), fileGroupSequence, request.getRemarksContent(),
                        latestProcessVersion.getMajorVersion(), latestProcessVersion.getMinorVersion() + 1);

        // 체크인 처리
        processMapper.updateProcessConfigManagementStatus(process.getProcessSequence(),
                ConfigManagementStatusCode.CHECK_IN.getCode(), AuthenticationTokenSupport.getUserId());

        return new ProcessCheckInResponse.Builder()
                .version(processVersion.getMajorVersion(), processVersion.getMinorVersion()).build();
    }

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.STUDIO_API)
    public ProcessCheckOutResponse checkOutProcess(ProcessCheckOutRequest request) {
        Process process = processMapper.selectByPrimaryKey(request.getProcessSequence());
        // 프로세스 존재 여부 확인
        if (process == null) {
            return ProcessCheckOutResponse.newErrorInstance(RpaApiError.PROCESS_NOT_FOUND);
        }

        // 체크인 상태 확인
        if (!ConfigManagementStatusCode.CHECK_IN.getCode().equalsIgnoreCase(process.getConfigManagementStatusCode())) {
            return ProcessCheckOutResponse.newErrorInstance(RpaApiError.ALREADY_CHECKED_OUT_PROCESS,
                    process.getConfigManagementUserId());
        }
        // 체크아웃 처리
        processMapper.updateProcessConfigManagementStatus(process.getProcessSequence(),
                ConfigManagementStatusCode.CHECK_OUT.getCode(), AuthenticationTokenSupport.getUserId());
        return new ProcessCheckOutResponse.Builder().processName(process.getProcessName())
                .processSequence(process.getProcessSequence())
                .configManagementStatusCode(ConfigManagementStatusCode.CHECK_OUT.getCode())
                .configManagementStatusCodeName(ConfigManagementStatusCode.CHECK_OUT.getDescription()).build();
    }


    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.STUDIO_API)
    public ProcessCheckOutResponse cancelCheckOutProcess(ProcessCheckOutRequest request) {
        Process process = processMapper.selectByPrimaryKey(request.getProcessSequence());
        // 프로세스 존재 여부 확인
        if (process == null) {
            return ProcessCheckOutResponse.newErrorInstance(RpaApiError.PROCESS_NOT_FOUND);
        }

        // 체크아웃 상태 확인
        if (!ConfigManagementStatusCode.CHECK_OUT.getCode().equalsIgnoreCase(process.getConfigManagementStatusCode())) {
            return ProcessCheckOutResponse.newErrorInstance(RpaApiError.ALREADY_CANCEL_CHECKED_OUT_PROCESS,
                    process.getConfigManagementUserId());
        }

        // 형상관리 사용자 확인
        if (!AuthenticationTokenSupport.getUserId().equals(process.getConfigManagementUserId())) {
            return ProcessCheckOutResponse.newErrorInstance(RpaApiError.NOT_MATCHED_CHECK_OUT_USER,
                    process.getConfigManagementUserId());
        }

        // 체크인 처리
        processMapper.updateProcessConfigManagementStatus(process.getProcessSequence(),
                ConfigManagementStatusCode.CHECK_IN.getCode(), AuthenticationTokenSupport.getUserId());

        return new ProcessCheckOutResponse.Builder().processName(process.getProcessName())
                .processSequence(process.getProcessSequence())
                .configManagementStatusCode(ConfigManagementStatusCode.CHECK_IN.getCode())
                .configManagementStatusCodeName(ConfigManagementStatusCode.CHECK_IN.getDescription()).build();
    }


    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.STUDIO_API)
    public ProcessListResponse getProcessInfoList() {
        List<ProcessInfo> processInfos = new ArrayList<>();
        for (Process process : processMapper.selectAll()) {
            processInfos.add(new ProcessInfo(process, processVersionMapper
                    .selectProcessVesionWithFileSequenceByProcessSequence(process.getProcessSequence())));
        }
        return new ProcessListResponse(processInfos);
    }

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.STUDIO_API)
    public ProcessHistoryResponse getProcessHistory(Integer processSequence) {
        List<ProcessVersion> processVersions = processVersionMapper.selectByProcessSequence(processSequence);
        List<Integer> processVersionSequences = new ArrayList<>();
        for (ProcessVersion processVersion : processVersions) {
            processVersionSequences.add(processVersion.getProcessVersionSequence());
        }

        List<ProcessHistoryInfo> processHistoryInfos = new ArrayList<>();
        if (processVersionSequences.size() > 0) {
            List<ProcessHistory> processHistories =
                    processHistoryMapper.selectByProcessVersionSequences(processVersionSequences);
            for (ProcessHistory processHistory : processHistories) {
                processHistoryInfos.add(new ProcessHistoryInfo(processHistory));
            }
        }
        return new ProcessHistoryResponse(processHistoryInfos);
    }

    private ProcessVersion insertProcessVersion(Integer processSequence, Integer fileGroupSequence,
            String remarksContent, int majorVersion, int minorVersion) {
        ProcessVersion processVersion = new ProcessVersion();
        processVersion.setProcessSequence(processSequence);
        processVersion.setMajorVersion(majorVersion);
        processVersion.setMinorVersion(minorVersion);
        processVersion.setRemarksContent(remarksContent);
        processVersion.setProcessFileGroupSequence(fileGroupSequence);
        processVersion.setActivationYn("N");
        processVersion.setRemoveYn("N");
        processVersion.setRegisterId(AuthenticationTokenSupport.getUserId());
        processVersionMapper.insert(processVersion);
        return processVersion;
    }

    private Integer insertProcess(String processName) {
        Process process = new Process();
        process.setProcessName(processName);
        process.setRegisterId(AuthenticationTokenSupport.getUserId());
        process.setConfigManagementStatusCode(ConfigManagementStatusCode.CHECK_IN.getCode());
        process.setConfigManagementUserId(AuthenticationTokenSupport.getUserId());
        processMapper.insert(process);
        return process.getProcessSequence();
    }
}
