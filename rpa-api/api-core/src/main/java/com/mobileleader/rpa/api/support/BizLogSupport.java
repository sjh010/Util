package com.mobileleader.rpa.api.support;

import com.mobileleader.rpa.api.data.dto.TaskQueueProcessInfo;
import com.mobileleader.rpa.api.data.mapper.biz.ProcessHistoryMapper;
import com.mobileleader.rpa.api.data.mapper.biz.RobotLogMapper;
import com.mobileleader.rpa.api.data.mapper.biz.StudioLogMapper;
import com.mobileleader.rpa.api.data.mapper.biz.TaskLogMapper;
import com.mobileleader.rpa.api.data.mapper.biz.TaskQueueMapper;
import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import com.mobileleader.rpa.data.dto.biz.ProcessHistory;
import com.mobileleader.rpa.data.dto.biz.RobotLog;
import com.mobileleader.rpa.data.dto.biz.StudioLog;
import com.mobileleader.rpa.data.dto.biz.TaskLog;
import com.mobileleader.rpa.data.dto.common.CommonFile;
import com.mobileleader.rpa.data.type.FileGroupColumdId;
import com.mobileleader.rpa.data.type.FileGroupTableId;
import com.mobileleader.rpa.data.type.ProcessHistoryTypeCode;
import com.mobileleader.rpa.data.type.RobotLogStatusCode;
import com.mobileleader.rpa.data.type.RobotLogTypeCode;
import com.mobileleader.rpa.data.type.StudioLogStatusCode;
import com.mobileleader.rpa.data.type.StudioLogTypeCode;
import com.mobileleader.rpa.data.type.TaskLogStatusCode;
import com.mobileleader.rpa.repository.file.FileGroupRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class BizLogSupport {

    @Autowired
    private StudioLogMapper studioLogMapper;

    @Autowired
    private RobotLogMapper robotLogMapper;

    @Autowired
    private TaskLogMapper taskLogMapper;

    @Autowired
    private TaskQueueMapper taskQueueMapper;

    @Autowired
    private ProcessHistoryMapper processHistoryMapper;

    @Autowired
    private FileGroupRepository fileGroupRepository;

    /**
     * 로봇 작업로그를 추가한다.
     *
     * @param robotLogTypeCode 로봇로그유형코드 {@link RobotLogTypeCode}
     * @param robotLogStatusCode 로봇로그상태코드 {@link RobotLogStatusCode}
     * @param robotSequence 로봇순번
     * @param robotName 로봇명
     * @param remarksContent 비고내용
     * @return
     */
    @Transactional
    public Integer addRobotLog(RobotLogTypeCode robotLogTypeCode, RobotLogStatusCode robotLogStatusCode,
            Integer robotSequence, String robotName, String remarksContent) {
        return insertRobotLog(robotLogTypeCode, robotLogStatusCode, robotSequence, robotName, remarksContent, null);
    }

    /**
     * 로봇 작업로그를 추가한다.
     *
     * @param robotLogTypeCode 로봇로그유형코드 {@link RobotLogTypeCode}
     * @param robotLogStatusCode 로봇로그상태코드 {@link RobotLogStatusCode}
     * @param remarksContent 비고내용
     * @return robotLogSequence
     */
    @Transactional
    public Integer addRobotLog(RobotLogTypeCode robotLogTypeCode, RobotLogStatusCode robotLogStatusCode,
            String remarksContent) {
        return insertRobotLog(robotLogTypeCode, robotLogStatusCode,
                AuthenticationTokenSupport.getAuthenticationSequence(AuthenticationType.ROBOT),
                AuthenticationTokenSupport.getUserName(), remarksContent, null);
    }

    /**
     * 로봇 작업로그를 추가한다.
     *
     * @param robotLogTypeCode 로봇로그유형코드 {@link RobotLogTypeCode}
     * @param robotLogStatusCode 로봇로그상태코드 {@link RobotLogStatusCode}
     * @param remarksContent 비고내용
     * @param multipartFiles 로그파일
     * @param fileRemarksContent 파일 비고내용
     * @return robotLogSequence
     */
    @Transactional
    public Integer addRobotLog(RobotLogTypeCode robotLogTypeCode, RobotLogStatusCode robotLogStatusCode,
            String remarksContent, MultipartFile[] multipartFiles, String fileRemarksContent) {
        Integer fileGroupSequence = null;
        if (multipartFiles != null) {
            fileGroupSequence = addLogFiles(multipartFiles, FileGroupTableId.ROBOT_LOG_TABLE,
                    FileGroupColumdId.LOG_FILE_GROUP_SEQUENCE, fileRemarksContent);
        }
        return insertRobotLog(robotLogTypeCode, robotLogStatusCode,
                AuthenticationTokenSupport.getAuthenticationSequence(AuthenticationType.ROBOT),
                AuthenticationTokenSupport.getUserName(), remarksContent, fileGroupSequence);
    }

    /**
     * 스튜디오 로그를 추가한다.
     *
     * @param studioLogTypeCode 스튜디오로그유형코드 {@link StudioLogTypeCode}
     * @param studioLogStatusCode 스튜디오로그상태코드 {@link StudioLogStatusCode}
     * @param userId 사용자ID
     * @param userName 사용자명
     * @param remarksContent 비고내용
     * @return studioLogSequence
     */
    @Transactional
    public Integer addStudioLog(StudioLogTypeCode studioLogTypeCode, StudioLogStatusCode studioLogStatusCode,
            String userId, String userName, String remarksContent) {
        return insertStudioLog(studioLogTypeCode, studioLogStatusCode, userId, userName, remarksContent, null);
    }

    /**
     * 스튜디오 로그를 추가한다.
     *
     * @param studioLogTypeCode 스튜디오로그유형코드 {@link StudioLogTypeCode}
     * @param studioLogStatusCode 스튜디오로그상태코드 {@link StudioLogStatusCode}
     * @param remarksContent 비고내용
     * @return studioLogSequence
     */
    @Transactional
    public Integer addStudioLog(StudioLogTypeCode studioLogTypeCode, StudioLogStatusCode studioLogStatusCode,
            String remarksContent) {
        return insertStudioLog(studioLogTypeCode, studioLogStatusCode, AuthenticationTokenSupport.getUserId(),
                AuthenticationTokenSupport.getUserName(), remarksContent, null);
    }

    /**
     * 스튜디오 로그를 추가한다.
     *
     * @param studioLogTypeCode 스튜디오로그유형코드 {@link StudioLogTypeCode}
     * @param studioLogStatusCode 스튜디오로그상태코드 {@link StudioLogStatusCode}
     * @param remarksContent 비고내용
     * @param multipartFiles 로그파일
     * @param fileRemarksContent 파일 비고내용
     * @return studioLogSequence
     */
    @Transactional
    public Integer addStudioLog(StudioLogTypeCode studioLogTypeCode, StudioLogStatusCode studioLogStatusCode,
            String remarksContent, MultipartFile[] multipartFiles, String fileRemarksContent) {
        Integer fileGroupSequence = null;
        if (multipartFiles != null) {
            fileGroupSequence = addLogFiles(multipartFiles, FileGroupTableId.STUDIO_LOG_TABLE,
                    FileGroupColumdId.LOG_FILE_GROUP_SEQUENCE, fileRemarksContent);
        }
        return insertStudioLog(studioLogTypeCode, studioLogStatusCode, AuthenticationTokenSupport.getUserId(),
                AuthenticationTokenSupport.getUserName(), remarksContent, fileGroupSequence);
    }

    /**
     * 작업 로그를 추가한다.
     *
     * @param taskQueueSequence 작업대기열순번
     * @param taskLogStatusCode 작업로그상태코드 {@link TaskLogStatusCode}
     * @param remarksContent 비고내용
     * @return taskLogSequence
     */
    @Transactional
    public Integer addTaskLog(Integer taskQueueSequence, TaskLogStatusCode taskLogStatusCode, String remarksContent) {
        return insertTaskLog(taskQueueSequence, taskLogStatusCode, null, null);
    }

    /**
     * 작업 로그를 추가한다.
     *
     * @param taskQueueSequence 작업대기열순번
     * @param taskLogStatusCode 작업로그상태코드 {@link TaskLogStatusCode}
     * @param remarksContent 비고내용
     * @param multipartFiles 로그파일
     * @param fileRemarksContent 파일 비고내용
     * @return taskLogSequence
     */
    @Transactional
    public Integer addTaskLog(Integer taskQueueSequence, TaskLogStatusCode taskLogStatusCode, String remarksContent,
            MultipartFile[] multipartFiles, String fileRemarksContent) {
        Integer fileGroupSequence = null;
        if (multipartFiles != null) {
            fileGroupSequence = addLogFiles(multipartFiles, FileGroupTableId.TASK_LOG_TABLE,
                    FileGroupColumdId.LOG_FILE_GROUP_SEQUENCE, fileRemarksContent);
        }
        return insertTaskLog(taskQueueSequence, taskLogStatusCode, fileGroupSequence, remarksContent);
    }

    /**
     * 프로세스 히스토리를 추가한다.
     *
     * @param processVersionSequence 프로세스버전순번
     * @param processHistoryTypeCode 프로세스히스토리유형코드 {@link ProcessHistoryTypeCode}
     * @param remarksContent 비고내용
     * @return processHistorySequence
     */
    public Integer addProcessHistory(Integer processVersionSequence, ProcessHistoryTypeCode processHistoryTypeCode,
            String remarksContent) {
        ProcessHistory processHistory = new ProcessHistory();
        processHistory.setProcessVersionSequence(processVersionSequence);
        processHistory.setRegisterId(AuthenticationTokenSupport.getUserId());
        processHistory.setRemarksContent(remarksContent);
        processHistory.setProcessHistoryTypeCode(processHistoryTypeCode.getCode());
        processHistoryMapper.insert(processHistory);
        return processHistory.getProcessHistorySequence();
    }

    private Integer addLogFiles(MultipartFile[] multipartFiles, FileGroupTableId fileGroupTableId,
            FileGroupColumdId fileGroupColumdId, String remarksContent) {
        Integer fileGroupSequence = null;
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                if (fileGroupSequence != null) {
                    fileGroupRepository.addFile(multipartFile, fileGroupSequence, remarksContent);
                } else {
                    CommonFile commonFile = fileGroupRepository.addFile(multipartFile, fileGroupTableId,
                            fileGroupColumdId, remarksContent);
                    fileGroupSequence = commonFile.getFileGroupSequence();
                }
            }
        } catch (IOException e) {
            throw new RpaApiException(RpaApiError.FILE_IO_ERROR);
        }
        return fileGroupSequence;
    }

    private int insertTaskLog(Integer taskQueueSequence, TaskLogStatusCode taskLogStatusCode,
            Integer logFileGroupSequence, String remarksContent) {
        TaskQueueProcessInfo taskQueueProcessInfo = taskQueueMapper.selectTaskQueueProcessInfo(taskQueueSequence);
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskQueueSequence(taskQueueSequence);
        taskLog.setProcessName(taskQueueProcessInfo.getProcessName());
        taskLog.setProcessVersion(taskQueueProcessInfo.getProcessVersion());
        taskLog.setProcessVersionSequence(taskQueueProcessInfo.getProcessVersionSequence());
        taskLog.setLogFileGroupSequence(logFileGroupSequence);
        taskLog.setTaskLogStatusCode(taskLogStatusCode.getCode());
        taskLog.setRemarksContent(remarksContent);
        taskLog.setRobotName(AuthenticationTokenSupport.getUserName());
        taskLogMapper.insert(taskLog);
        return taskLog.getTaskLogSequence();
    }

    private int insertStudioLog(StudioLogTypeCode studioLogTypeCode, StudioLogStatusCode studioLogStatusCode,
            String userId, String userName, String remarksContent, Integer logFileGroupSequence) {
        StudioLog studioLog = new StudioLog();
        studioLog.setStudioLogTypeCode(studioLogTypeCode.getCode());
        studioLog.setStudioUserId(userId);
        studioLog.setStudioUserName(userName);
        studioLog.setStudioLogStatusCode(studioLogStatusCode.getCode());
        studioLog.setRemarksContent(remarksContent);
        studioLog.setLogFileGroupSequence(logFileGroupSequence);
        studioLogMapper.insert(studioLog);
        return studioLog.getStudioLogSequence();
    }

    private int insertRobotLog(RobotLogTypeCode robotLogTypeCode, RobotLogStatusCode robotLogStatusCode,
            Integer robotSequence, String robotName, String remarksContent, Integer logFileGroupSequence) {
        RobotLog robotLog = new RobotLog();
        robotLog.setRobotLogTypeCode(robotLogTypeCode.getCode());
        robotLog.setRemarksContent(remarksContent);
        robotLog.setRobotLogStatusCode(robotLogStatusCode.getCode());
        robotLog.setRobotSequence(robotSequence);
        robotLog.setRobotName(robotName);
        robotLog.setLogFileGroupSequence(logFileGroupSequence);
        robotLogMapper.insert(robotLog);
        return robotLog.getRobotLogSequence();
    }
}
