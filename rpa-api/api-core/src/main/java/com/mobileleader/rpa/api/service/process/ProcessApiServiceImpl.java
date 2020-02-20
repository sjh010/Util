package com.mobileleader.rpa.api.service.process;

import com.mobileleader.rpa.api.data.mapper.biz.ProcessVersionMapper;
import com.mobileleader.rpa.api.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.api.data.mapper.biz.TaskQueueMapper;
import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.api.model.request.ProcessStatusRequest;
import com.mobileleader.rpa.api.model.response.ProcessDownloadResponse;
import com.mobileleader.rpa.api.model.response.ProcessTaskResponse;
import com.mobileleader.rpa.api.support.AuthenticationTokenSupport;
import com.mobileleader.rpa.api.support.BizLogSupport;
import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import com.mobileleader.rpa.data.dto.biz.TaskQueue;
import com.mobileleader.rpa.data.dto.common.CommonFile;
import com.mobileleader.rpa.data.type.ProcessHistoryTypeCode;
import com.mobileleader.rpa.data.type.TaskLogStatusCode;
import com.mobileleader.rpa.data.type.TaskStatusCode;
import com.mobileleader.rpa.repository.file.FileGroupRepository;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@Service
public class ProcessApiServiceImpl implements ProcessApiService {

    @Autowired
    private TaskQueueMapper taskQueueMapper;

    @Autowired
    private ProcessVersionMapper processVersionMapper;

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private FileGroupRepository fileGroupRepository;

    @Autowired
    private BizLogSupport bizLogSupport;

    @Override
    @Secured(RpaAuthority.SecuredRole.ROBOT_API)
    @Transactional
    public ProcessTaskResponse getProcessTask(Integer robotSequence) {
        TaskQueue taskQueue =
                taskQueueMapper.selectTopByRobotSequenceAndTaskStatusCode(robotSequence, TaskStatusCode.WAIT.getCode());
        if (ObjectUtils.isEmpty(taskQueue)) {
            return ProcessTaskResponse.newEmptyInstance();
        }
        ProcessVersion processVersion = processVersionMapper.selectByPrimaryKey(taskQueue.getProcessVersionSequence());
        int majorVersion = -1;
        int minorVersion = -1;
        if (processVersion != null) {
            majorVersion = processVersion.getMajorVersion();
            minorVersion = processVersion.getMinorVersion();
        }
        taskQueueMapper.updateTaskStatusCode(taskQueue.getTaskQueueSequence(), TaskStatusCode.DELIVERY.getCode());
        bizLogSupport.addTaskLog(taskQueue.getTaskQueueSequence(), TaskLogStatusCode.TASK_DELIVERY, null);
        return ProcessTaskResponse
                .newInstance(new ProcessTaskResponse.Builder().taskQueueSequence(taskQueue.getTaskQueueSequence())
                        .processVersionSequence(taskQueue.getProcessVersionSequence())
                        .fileSequenceList(fileGroupRepository
                                .getFileSequencesByProcessVersionSequence(taskQueue.getProcessVersionSequence()))
                        .processVersion(majorVersion, minorVersion));
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.ROBOT_API)
    @Transactional
    public void addProcessTaskLog(ProcessStatusRequest processStatusRequest) {

        Integer taskQueueSequence = processStatusRequest.getTaskQueueSequence();

        TaskQueue taskQueue = taskQueueMapper.selectByPrimaryKey(taskQueueSequence);
        Assert.notNull(taskQueue, "taskQueue not found");

        TaskLogStatusCode taskLogStatusCode = TaskLogStatusCode.getByCode(processStatusRequest.getTaskLogStatusCode());
        switch (taskLogStatusCode) {
            case PROCESS_EXECUTE_START:
                updateRobotLastExecuteProcess(taskQueue.getRobotSequence(), taskQueue.getProcessVersionSequence());
                taskQueueMapper.updateLastExecuteDateTime(taskQueueSequence);
                break;
            case PROCESS_EXECUTE_SUCCESS:
                taskQueueMapper.updateTaskStatusCode(taskQueueSequence, TaskStatusCode.SUCCESS.getCode());
                break;
            case PROCESS_EXECUTE_FAILURE:
                taskQueueMapper.updateTaskStatusCode(taskQueueSequence, TaskStatusCode.FAILURE.getCode());
                break;
            case UNKNOWN:
                throw new RpaApiException(RpaApiError.INVALID_PARAMETER);
            default:
                break;
        }
        bizLogSupport.addTaskLog(taskQueueSequence, taskLogStatusCode, processStatusRequest.getRemarksContent(),
                processStatusRequest.getLogFiles(), processStatusRequest.getLogFileRemarksContent());
    }

    @Override
    @Secured({RpaAuthority.SecuredRole.ROBOT_API, RpaAuthority.SecuredRole.STUDIO_API})
    @Transactional
    public ProcessDownloadResponse downloadProcessFile(Integer processVersionSequence, Integer fileSequence,
            HttpServletRequest request, HttpServletResponse response) {

        ProcessVersion processVersion = processVersionMapper.selectByPrimaryKey(processVersionSequence);

        // 프로세스 버전 존재 여부 확인
        if (processVersion == null) {
            return ProcessDownloadResponse.newErrorInstance(RpaApiError.PROCESS_NOT_FOUND);
        }

        // 프로세스 삭제 여부 확인
        if ("Y".equalsIgnoreCase(processVersion.getRemoveYn())) {
            return ProcessDownloadResponse.newErrorInstance(RpaApiError.REMOVED_PROCESS);
        }

        CommonFile commonFile = fileGroupRepository.getCommonFile(fileSequence);

        if (commonFile == null) {
            return ProcessDownloadResponse.newErrorInstance(RpaApiError.PROCESS_FILE_NOT_FOUND);
        }

        // 해당 프로세스의 파일이 맞는지 확인
        if (!(commonFile.getFileGroupSequence().equals(processVersion.getProcessFileGroupSequence()))) {
            return ProcessDownloadResponse.newErrorInstance(RpaApiError.NOT_MATCHED_PROCESS_FILE_SEQUENCE);
        }

        byte[] data = null;
        try {
            data = fileGroupRepository.getFile(commonFile.getFilePath());
            Assert.notNull(data, "file not found");
            setFileDownloadHeader(request, response, commonFile.getFileName(), data.length);
            writeBinaryResponse(response, data);
        } catch (IOException e) {
            throw new RpaApiException(RpaApiError.FILE_IO_ERROR);
        }

        ProcessHistoryTypeCode processHistoryTypeCode = ProcessHistoryTypeCode.UNKNOWN;
        if (AuthenticationTokenSupport.isRobot()) {
            processHistoryTypeCode = ProcessHistoryTypeCode.DOWNLOAD_ROBOT;
        } else if (AuthenticationTokenSupport.isStudio()) {
            processHistoryTypeCode = ProcessHistoryTypeCode.DOWNLOAD_STUDIO;
        }

        // 히스토리 등록
        bizLogSupport.addProcessHistory(processVersion.getProcessVersionSequence(), processHistoryTypeCode, null);
        return new ProcessDownloadResponse();
    }


    private void updateRobotLastExecuteProcess(Integer robotSequence, Integer processVersionSequence) {
        ProcessVersion processVersion = processVersionMapper.selectByPrimaryKey(processVersionSequence);
        robotMapper.updateRobotLastExecuteProcessSequence(robotSequence, processVersion.getProcessSequence());
    }

    private void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName,
            long fileSize) {

        String encodedFilename = null;

        try {
            encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RpaApiException(RpaApiError.ENCODE_ERROR);
        }

        response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFilename + "\"");
        response.setContentLength((int) fileSize);
    }

    private void writeBinaryResponse(HttpServletResponse response, byte[] data) {
        try (OutputStream os = response.getOutputStream()) {
            os.write(data);
            os.flush();
        } catch (IOException e) {
            throw new RpaApiException(RpaApiError.FILE_IO_ERROR);
        }
    }
}
