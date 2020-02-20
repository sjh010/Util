package com.mobileleader.rpa.api.service.log;

import com.mobileleader.rpa.api.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.api.model.request.LogUploadRequest;
import com.mobileleader.rpa.api.schedule.ScheduleManager;
import com.mobileleader.rpa.api.schedule.job.ScheduleJobType;
import com.mobileleader.rpa.api.support.AuthenticationTokenSupport;
import com.mobileleader.rpa.api.support.BizLogSupport;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.type.RobotLogStatusCode;
import com.mobileleader.rpa.data.type.RobotLogTypeCode;
import com.mobileleader.rpa.data.type.RobotStatusCode;
import com.mobileleader.rpa.data.type.StudioLogStatusCode;
import com.mobileleader.rpa.data.type.StudioLogTypeCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class LogUploadServiceImpl implements LogUploadService {

    @Autowired
    private BizLogSupport bizLogSupport;

    @Autowired
    private ScheduleManager scheduleManager;

    @Autowired
    private RobotMapper robotMapper;

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.STUDIO_API)
    public void uploadStudioLog(LogUploadRequest request) {
        StudioLogTypeCode studioLogTypeCode = StudioLogTypeCode.getByCode(request.getLogTypeCode());
        Assert.isTrue(!StudioLogTypeCode.UNKNOWN.equals(studioLogTypeCode),
                "Unknown StudioLogTypeCode : " + request.getLogTypeCode());
        StudioLogStatusCode studioLogStatusCode = StudioLogStatusCode.getByCode(request.getLogStatusCode());
        Assert.isTrue(!StudioLogStatusCode.UNKNOWN.equals(studioLogStatusCode),
                "Unknown StudioLogStatusCode : " + request.getLogStatusCode());
        bizLogSupport.addStudioLog(studioLogTypeCode, studioLogStatusCode, request.getRemarksContent(),
                request.getLogFiles(), request.getLogFileRemarksContent());
    }

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.ROBOT_API)
    public void uploadRobotLog(LogUploadRequest request) {
        RobotLogTypeCode robotLogTypeCode = RobotLogTypeCode.getByCode(request.getLogTypeCode());
        Assert.isTrue(!RobotLogTypeCode.UNKNOWN.equals(robotLogTypeCode),
                "Unknown RobotLogTypeCode : " + request.getLogTypeCode());
        if (RobotLogTypeCode.DISCONNECT_BY_ROBOT.equals(robotLogTypeCode)) {
            updateRobotStatusToDisconnectByRobot();
        }
        RobotLogStatusCode robotLogStatusCode = RobotLogStatusCode.getByCode(request.getLogStatusCode());
        Assert.isTrue(!RobotLogStatusCode.UNKNOWN.equals(robotLogStatusCode),
                "Unknown RobotLogStatusCode : " + request.getLogStatusCode());
        bizLogSupport.addRobotLog(robotLogTypeCode, robotLogStatusCode, request.getRemarksContent(),
                request.getLogFiles(), request.getLogFileRemarksContent());
    }

    private void updateRobotStatusToDisconnectByRobot() {
        Integer robotSequence = AuthenticationTokenSupport.getAuthenticationSequence(AuthenticationType.ROBOT);
        scheduleManager.deleteJob(ScheduleJobType.ROBOT_STATUS_UPDATE.getJobKey(robotSequence.toString()));
        robotMapper.updateRobotStatusCode(robotSequence, RobotStatusCode.DISCONNECT_BY_ROBOT.getCode());
    }
}
