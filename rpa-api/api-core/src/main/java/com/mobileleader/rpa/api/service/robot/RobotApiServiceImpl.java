package com.mobileleader.rpa.api.service.robot;

import com.mobileleader.rpa.api.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.api.model.request.RobotStatusRequest;
import com.mobileleader.rpa.api.schedule.ScheduleManager;
import com.mobileleader.rpa.api.schedule.job.RobotStatusUpdateJob;
import com.mobileleader.rpa.api.schedule.job.ScheduleJobType;
import com.mobileleader.rpa.api.schedule.trigger.CronTriggerBuilder;
import com.mobileleader.rpa.api.support.AuthenticationTokenSupport;
import com.mobileleader.rpa.api.support.BizLogSupport;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.type.RobotLogStatusCode;
import com.mobileleader.rpa.data.type.RobotLogTypeCode;
import com.mobileleader.rpa.data.type.RobotStatusCode;
import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import org.joda.time.DateTime;
import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RobotApiServiceImpl implements RobotApiService {

    @Autowired
    private ScheduleManager scheduleManager;

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private BizLogSupport bizLogSupport;

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.ROBOT_API)
    public void updateRobotStatusCode(RobotStatusRequest robotStatusRequest) {
        RobotStatusCode robotStatusCode = RobotStatusCode.getByCode(robotStatusRequest.getRobotStatusCode());
        if (RobotStatusCode.UNKNOWN.equals(robotStatusCode)) {
            throw new RpaApiException(RpaApiError.INVALID_PARAMETER);
        }
        Integer robotSequence = AuthenticationTokenSupport.getAuthenticationSequence(AuthenticationType.ROBOT);
        scheduleManager.deleteJob(ScheduleJobType.ROBOT_STATUS_UPDATE.getJobKey(robotSequence.toString()));
        robotMapper.updateRobotStatusCode(robotSequence, robotStatusRequest.getRobotStatusCode());
        if (RobotStatusCode.DISCONNECT_BY_ROBOT.equals(robotStatusCode)) {
            bizLogSupport.addRobotLog(RobotLogTypeCode.DISCONNECT_BY_ROBOT, RobotLogStatusCode.SUCCESS, null);
        } else {
            createRobotStatusSetDisconnectSchedule(robotSequence, robotStatusRequest.getHealthCheckInterval());
        }
    }

    // 로봇 상태 Disconnect 스케줄러 생성
    private boolean createRobotStatusSetDisconnectSchedule(Integer robotSequence, String healthCheckIntervalValue) {
        ScheduleJobType scheduleJobType = ScheduleJobType.ROBOT_STATUS_UPDATE;
        String triggerName = scheduleJobType.getTriggerNamePrefix() + robotSequence.toString();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(RobotStatusUpdateJob.JobDataKey.ROBOT_SEQUENCE.name(), robotSequence);
        jobDataMap.put(RobotStatusUpdateJob.JobDataKey.ROBOT_NAME.name(), AuthenticationTokenSupport.getUserName());
        jobDataMap.put(RobotStatusUpdateJob.JobDataKey.ROBOT_LOG_TYPE_CODE.name(),
                RobotLogTypeCode.DISCONNECT_BY_SERVER.getCode());
        jobDataMap.put(RobotStatusUpdateJob.JobDataKey.ROBOT_STATUS_CODE.name(),
                RobotStatusCode.DISCONNECT_BY_SERVER.getCode());

        DateTime executeDateTime = DateTime.now().plusMinutes(1);
        Trigger trigger = CronTriggerBuilder.createCronTrigger(new CronTriggerBuilder.Builder().name(triggerName)
                .group(scheduleJobType.getTriggerGroup()).jobDataMap(jobDataMap)
                .cronExpression(DateTimeUtils.toCronExpression(executeDateTime)).build());
        return scheduleManager.addSchedule(ScheduleJobType.ROBOT_STATUS_UPDATE.getJobDetail(robotSequence.toString()),
                trigger);
    }
}
