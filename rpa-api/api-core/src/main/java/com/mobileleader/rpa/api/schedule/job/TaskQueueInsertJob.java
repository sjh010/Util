package com.mobileleader.rpa.api.schedule.job;

import com.mobileleader.rpa.api.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.api.data.mapper.biz.TaskQueueMapper;
import com.mobileleader.rpa.data.dto.biz.Robot;
import com.mobileleader.rpa.data.dto.biz.TaskQueue;
import com.mobileleader.rpa.data.type.RobotStatusCode;
import com.mobileleader.rpa.data.type.TaskStatusCode;
import com.mobileleader.rpa.utils.json.JsonConverter;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TaskQueueInsertJob extends AbstractRpaJob {
    private static final Logger logger = LoggerFactory.getLogger(TaskQueueInsertJob.class);

    @Autowired
    private TaskQueueMapper taskQueueMapper;

    @Autowired
    private RobotMapper robotMapper;

    public enum JobDataKey {
        ROBOT_SEQUENCE, WORK_SEQUENCE, PROCESS_VERSION_SEQUENCE;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Integer workSequence = getTriggerJobData(context, JobDataKey.WORK_SEQUENCE.name());
        Integer robotSequence = getTriggerJobData(context, JobDataKey.ROBOT_SEQUENCE.name());
        Integer processVersionSequence = getTriggerJobData(context, JobDataKey.PROCESS_VERSION_SEQUENCE.name());
        DateTime lastFireDateTime = getFireTime(context);
        DateTime nextFireDateTime = getNextFireTime(context);

        TaskQueue taskQueue = new TaskQueue();
        taskQueue.setWorkSequence(workSequence);
        taskQueue.setRobotSequence(robotSequence);
        taskQueue.setProcessVersionSequence(processVersionSequence);
        taskQueue.setLastExecuteDateTime(lastFireDateTime);
        taskQueue.setNextExecuteDateTime(nextFireDateTime);

        TaskStatusCode taskStatusCode = TaskStatusCode.ERROR;
        // Job 실행 시점에 로봇이 연결되어 있지 않으면 상태코드 ERR
        Robot robot = robotMapper.selectByPrimaryKey(robotSequence);
        if (robot != null && isConnected(RobotStatusCode.getByCode(robot.getRobotStatusCode()))) {
            taskStatusCode = TaskStatusCode.WAIT;
        }
        taskQueue.setTaskStatusCode(taskStatusCode.getCode());
        logger.info(JsonConverter.toPrettyJson(taskQueue));
        taskQueueMapper.insert(taskQueue);
    }

    private boolean isConnected(RobotStatusCode robotStatusCode) {
        boolean isConnected = false;
        switch (robotStatusCode) {
            case CONNECT:
            case WORKING:
                isConnected = true;
                break;
            default:
                isConnected = false;
                break;
        }
        return isConnected;
    }
}
