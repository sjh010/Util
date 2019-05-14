package com.mobileleader.rpa.view.service.process;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.biz.ProcessHistory;
import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import com.mobileleader.rpa.data.dto.common.CommonCode;
import com.mobileleader.rpa.data.type.ConfigManagementStatusCode;
import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.data.type.ProcessHistoryTypeCode;
import com.mobileleader.rpa.repository.file.FileGroupRepository;
import com.mobileleader.rpa.repository.file.ZipFileInfo;
import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import com.mobileleader.rpa.view.data.dto.ProcessNameVersion;
import com.mobileleader.rpa.view.data.mapper.biz.ProcessHistoryMapper;
import com.mobileleader.rpa.view.data.mapper.biz.ProcessMapper;
import com.mobileleader.rpa.view.data.mapper.biz.ProcessVersionMapper;
import com.mobileleader.rpa.view.data.mapper.common.CommonCodeMapper;
import com.mobileleader.rpa.view.exception.RpaViewError;
import com.mobileleader.rpa.view.exception.RpaViewException;
import com.mobileleader.rpa.view.model.form.ProcessHistorySearchForm;
import com.mobileleader.rpa.view.model.form.ProcessSearchForm;
import com.mobileleader.rpa.view.model.request.ProcessDeleteRequest;
import com.mobileleader.rpa.view.model.response.ProcessHistoryListResponse;
import com.mobileleader.rpa.view.model.response.ProcessListResponse;
import com.mobileleader.rpa.view.support.UserDetailsSupport;
import com.mobileleader.rpa.view.util.FileDownloadUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class ProcessWebServiceImpl implements ProcessWebService {

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProcessVersionMapper processVersionMapper;

    @Autowired
    private ProcessHistoryMapper processHistoryMapper;

    @Autowired
    private CommonCodeMapper commonCodeMapper;

    @Autowired
    private FileGroupRepository fileGroupRepository;

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_READ)
    @Transactional
    public ProcessVersion getProcessVersion(Integer processVersionSequence) {
        return processVersionMapper.selectByPrimaryKey(processVersionSequence);
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_READ)
    @Transactional
    public ProcessListResponse getProcessList(ProcessSearchForm processSearchForm) {

        ProcessListResponse response = new ProcessListResponse();
        Integer count = processMapper.selectCount(processSearchForm);
        response.setCount(count);
        processSearchForm.resetPageNoIfNotValid(count);

        response.setProcessList(processMapper.selectProcessList(processSearchForm));
        response.setForm(processSearchForm);
        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_READ)
    @Transactional
    public ProcessHistoryListResponse getProcessHistory(ProcessHistorySearchForm processHistorySearchForm) {

        ProcessHistoryListResponse response = new ProcessHistoryListResponse();
        int count = processVersionMapper.selectCount(processHistorySearchForm);
        response.setCount(count);

        processHistorySearchForm.resetPageNoIfNotValid(count);
        response.setProcessHistoryList(processVersionMapper.selectProcessHistory(processHistorySearchForm));
        response.setForm(processHistorySearchForm);
        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_MODIFY)
    @Transactional
    public Integer checkInProcess(List<Integer> processVersionSequenceList) {
        Assert.notNull(processVersionSequenceList, "processVersionSequenceList must not be null");

        int count = 0;

        for (Integer processVersionSequence : processVersionSequenceList) {
            if (processMapper.updateConfigManagement(ConfigManagementStatusCode.CHECK_IN.getCode(),
                    processVersionSequence) > 0) {
                count++;
            }
        }

        return count;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_MODIFY)
    @Transactional
    public Integer setProcessStatusOff(Integer processVersionSequence) {

        addProcessHistory(processVersionSequence, ProcessHistoryTypeCode.STATUS_USE_NO.getCode());

        return processVersionMapper.updateActivationYn("N", processVersionSequence);
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_MODIFY)
    @Transactional
    public Integer setProcessStatusOn(Integer processVersionSequence) {

        ProcessVersion processVersion = processVersionMapper.selectByPrimaryKey(processVersionSequence);

        int result = 0;

        // 상용버전(x.0)의 경우 : 사용상태만 변경
        if (processVersion.getMinorVersion() == 0) {
            result = processVersionMapper.updateActivationYn("Y", processVersionSequence);
            addProcessHistory(processVersionSequence, ProcessHistoryTypeCode.STATUS_USE_YES.getCode());
        } else { // 상용버전이 아닐 경우 : 새로운 상용버전 추가
            processVersion
                    .setMajorVersion(processVersionMapper.selectNextMajorVersion(processVersion.getProcessSequence()));
            processVersion.setMinorVersion(0);
            processVersion.setActivationYn("Y");
            processVersion.setRemoveYn("N");
            processVersion.setRegisterId(UserDetailsSupport.getUserId());
            processVersion.setRemarksContent(null);

            result = processVersionMapper.insertProcessVersion(processVersion);
            addProcessHistory(processVersion.getProcessVersionSequence(),
                    ProcessHistoryTypeCode.STATUS_USE_YES.getCode());
        }

        return result;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_MODIFY)
    @Transactional
    public Integer deleteProcess(ProcessDeleteRequest processDeleteRequest) {
        List<Integer> processVersionSequenceList = processDeleteRequest.getProcessVersionSequenceList();
        List<Integer> processSeuqneceList = processDeleteRequest.getProcessSequenceList();

        int count = 0;

        String removeId = UserDetailsSupport.getUserId();

        // 프로세스 버전 삭제
        for (Integer processVersionSequence : processVersionSequenceList) {
            if (processVersionMapper.updateRemoveStatus(processVersionSequence, "Y", removeId) > 0) {
                count++;
                addProcessHistory(processVersionSequence, ProcessHistoryTypeCode.REMOVE.getCode());
            }
        }

        // 해당 프로세스의 모든 프로세스 버전이 삭제되었을 경우, 프로세스 삭제
        for (Integer processSequence : processSeuqneceList) {
            if (processVersionMapper.selectNotDeletedProcessCount(processSequence) == 0) {
                processMapper.updateRemoveYn(processSequence, "Y", removeId);
            }
        }

        return count;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_READ)
    @Transactional
    public void downloadProcess(List<Integer> processVersionSequenceList, HttpServletRequest request,
            HttpServletResponse response) {
        Assert.notNull(processVersionSequenceList, "processVersionSequenceList must not be null");

        List<ProcessVersion> processVersions = new ArrayList<>();
        List<ZipFileInfo> zipFileInfos = new ArrayList<>();
        for (Integer processVersionSequence : processVersionSequenceList) {
            ProcessVersion processVersion = processVersionMapper.selectByPrimaryKey(processVersionSequence);
            if (processVersion != null) {
                processVersions.add(processVersion);
                zipFileInfos
                        .add(new ZipFileInfo.Builder().fileGroupSequence(processVersion.getProcessFileGroupSequence())
                                .fileNamePostFix(createFileNameWithVersion(processVersion)).build());
            }
        }

        String zipName = "pkg_" + DateTimeUtils.getNowString("yyyyMMdd_HHmmss") + ".zip";

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            fileGroupRepository.getZipFile(zipFileInfos, bos);
        } catch (IOException e) {
            throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR);
        }
        FileDownloadUtils.setFileDownloadHeader(request, response, zipName);
        FileDownloadUtils.writeBinaryResponse(response, bos.toByteArray());

        for (ProcessVersion processVersion : processVersions) {
            addProcessHistory(processVersion.getProcessVersionSequence(),
                    ProcessHistoryTypeCode.DOWNLOAD_MANAGER.getCode());
        }
    }

    private String createFileNameWithVersion(ProcessVersion processVersion) {
        return new StringBuilder().append("_").append(processVersion.getMajorVersion()).append(".")
                .append(processVersion.getMinorVersion()).toString();
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_READ)
    @Transactional
    public List<CommonCode> getConfigManagementStatusCodeList() {
        return commonCodeMapper.selectByGroupCode(GroupCode.CONFIGURATION_MANAGEMENT_STATUS_CODE.getCode());
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_PROCESS_READ)
    @Transactional(readOnly = true)
    public List<ProcessNameVersion> findActiveProcessVersion(List<Integer> processVersionSequenceList) {

        List<ProcessNameVersion> activeProcessList = new ArrayList<>();
        List<ProcessNameVersion> processNameVersionList = processVersionMapper.selectActiveProcess(null);

        for (int i = 0; i < processVersionSequenceList.size(); i++) {
            Integer processVersionSequnce = processVersionSequenceList.get(i);

            ProcessNameVersion processVersion = findProcessVersionBySeq(processNameVersionList, processVersionSequnce);
            if (processVersion != null) {
                activeProcessList.add(processVersion);
            }

        }

        return null;
    }

    private void addProcessHistory(Integer processVersionSequence, String processHistoryTypeCode) {
        ProcessHistory history = new ProcessHistory();

        history.setProcessHistoryTypeCode(processHistoryTypeCode);
        history.setProcessVersionSequence(processVersionSequence);
        history.setRegisterId(UserDetailsSupport.getUserId());

        processHistoryMapper.insertHistory(history);
    }

    private ProcessNameVersion findProcessVersionBySeq(List<ProcessNameVersion> processNameVersionList,
            Integer processVersionSequnce) {
        for (int i = 0; i < processNameVersionList.size(); i++) {
            ProcessNameVersion processNameVersion = processNameVersionList.get(i);
            if (processNameVersion.getProcessVersionSequence().equals(processVersionSequnce)) {
                return processNameVersion;
            }
        }
        return null;
    }

}
