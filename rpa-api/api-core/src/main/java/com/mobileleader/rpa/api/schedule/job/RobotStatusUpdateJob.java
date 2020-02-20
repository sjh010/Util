package com.mobileleader.rpa.api.schedule.job;

import com.mobileleader.rpa.api.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.api.support.BizLogSupport;
import com.mobileleader.rpa.data.type.RobotLogStatusCode;
import com.mobileleader.rpa.data.type.RobotLogTypeCode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RobotStatusUpdateJob extends AbstractRpaJob {
    private static final Logger logger = LoggerFactory.getLogger(RobotStatusUpdateJob.class);

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private BizLogSupport bizLogSupport;

    public enum JobDataKey {
        ROBOT_SEQUENCE, ROBOT_STATUS_CODE, ROBOT_LOG_TYPE_CODE, ROBOT_NAME;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Integer robotSequence = getTriggerJobData(context, JobDataKey.ROBOT_SEQUENCE.name());
        String robotName = getTriggerJobData(context, JobDataKey.ROBOT_NAME.name());
        String robotStatusCode = getTriggerJobData(context, JobDataKey.ROBOT_STATUS_CODE.name());
        String robotLogTypeCode = getTriggerJobData(context, JobDataKey.ROBOT_LOG_TYPE_CODE.name());
        logger.info("[Robot Status Update] robotSequence : {}, robotLogTypeCode : {}, robotStatusCode : {}",
                robotSequence, robotLogTypeCode, robotStatusCode);
        if (robotSequence != null && robotLogTypeCode != null) {
            robotMapper.updateRobotStatusCode(robotSequence, robotStatusCode);
        }
        bizLogSupport.addRobotLog(RobotLogTypeCode.getByCode(robotLogTypeCode), RobotLogStatusCode.SUCCESS,
                robotSequence, robotName, null);
    }
}
