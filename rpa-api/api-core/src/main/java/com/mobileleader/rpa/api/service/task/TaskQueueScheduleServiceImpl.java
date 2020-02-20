package com.mobileleader.rpa.api.service.task;

import com.mobileleader.rpa.api.data.mapper.biz.WorkAssignmentMapper;
import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.api.model.request.DailyScheduleRequest;
import com.mobileleader.rpa.api.model.request.HourlyScheduleRequest;
import com.mobileleader.rpa.api.model.request.TaskQueueControlRequest;
import com.mobileleader.rpa.api.model.request.TaskQueueScheduleRequest;
import com.mobileleader.rpa.api.model.request.WeeklyScheduleRequest;
import com.mobileleader.rpa.api.schedule.ScheduleManager;
import com.mobileleader.rpa.api.schedule.job.ScheduleJobType;
import com.mobileleader.rpa.api.schedule.job.TaskQueueInsertJob;
import com.mobileleader.rpa.api.schedule.trigger.CronTriggerBuilder;
import com.mobileleader.rpa.api.schedule.trigger.SimpleTriggerBuilder;
import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.biz.WorkAssignment;
import com.mobileleader.rpa.data.type.RepeatCycleUnitCode;
import java.util.List;
import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskQueueScheduleServiceImpl implements TaskQueueScheduleService {

    @Autowired
    private ScheduleManager scheduleManager;

    @Autowired
    private WorkAssignmentMapper workAssignmentMapper;

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    public String createHourlyWorkAssignmentSchedule(HourlyScheduleRequest request) {
        return createSchedule(RepeatCycleUnitCode.TIME, request.getWorkSequence(), request.getRobotSequence(),
                request.getProcessVersionSequence(), -1, request.getMinute(), null);
    }

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    public String createDailyWorkAssignmentSchedule(DailyScheduleRequest request) {
        return createSchedule(RepeatCycleUnitCode.DAY, request.getWorkSequence(), request.getRobotSequence(),
                request.getProcessVersionSequence(), request.getHour(), request.getMinute(), null);
    }

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    public String createWeeklyWorkAssignmentSchedule(WeeklyScheduleRequest request) {
        return createSchedule(RepeatCycleUnitCode.WEEK, request.getWorkSequence(), request.getRobotSequence(),
                request.getProcessVersionSequence(), request.getHour(), request.getMinute(), request.getDaysOfWeek());
    }

    @Override
    @Transactional
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    public boolean removeWorkAssignmentSchedule(TaskQueueControlRequest request) {
        List<WorkAssignment> workAssignments = workAssignmentMapper.selectByWorkSequence(request.getWorkSequence());
        for (WorkAssignment workAssignment : workAssignments) {
            if ("Y".equals(workAssignment.getSchedulerTriggerRegistYn())
                    && workAssignment.getSchedulerTriggerName() != null) {
                scheduleManager.deleteJob(
                        ScheduleJobType.TASK_QUEUE_INSERT.getJobKey(workAssignment.getWorkSequence().toString() + "_"
                                + workAssignment.getRobotSequence().toString()));
            }
        }
        return true;
    }

    private String createSchedule(RepeatCycleUnitCode repeatCycleUnitCode, Integer workSequence, Integer robotSequence,
            Integer processVersionSequence, int hour, int minute, Integer[] daysOfWeek) {
        JobDataMap jobDataMap = createTaskQueueScheduleJobDataMap(workSequence, robotSequence, processVersionSequence);
        String triggerName = createTriggerName(workSequence, robotSequence);
        Trigger trigger = null;
        switch (repeatCycleUnitCode) {
            case TIME:
                trigger = createHourlyTrigger(triggerName, jobDataMap, minute);
                break;
            case DAY:
                trigger = createDailyTrigger(triggerName, jobDataMap, hour, minute);
                break;
            case WEEK:
                trigger = createWeeklyTrigger(triggerName, jobDataMap, hour, minute, daysOfWeek);
                break;
            default:
                throw new RpaApiException(RpaApiError.INVALID_PARAMETER);
        }
        if (!scheduleManager.addSchedule(ScheduleJobType.TASK_QUEUE_INSERT
                .getJobDetail(workSequence.toString() + "_" + robotSequence.toString()), trigger)) {
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR);
        }
        return triggerName;
    }

    private <T extends TaskQueueScheduleRequest> JobDataMap createTaskQueueScheduleJobDataMap(Integer workSequence,
            Integer robotSequence, Integer processVersionSequence) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(TaskQueueInsertJob.JobDataKey.WORK_SEQUENCE.name(), workSequence);
        jobDataMap.put(TaskQueueInsertJob.JobDataKey.ROBOT_SEQUENCE.name(), robotSequence);
        jobDataMap.put(TaskQueueInsertJob.JobDataKey.PROCESS_VERSION_SEQUENCE.name(), processVersionSequence);
        return jobDataMap;
    }

    private String createTriggerName(Integer workSequence, Integer robotSequence) {
        return new StringBuilder(ScheduleJobType.TASK_QUEUE_INSERT.getTriggerNamePrefix())
                .append(workSequence.toString()).append("_").append(robotSequence).toString();
    }

    private Trigger createWeeklyTrigger(String triggerName, JobDataMap jobDataMap, int hour, int minute,
            Integer[] daysOfWeek) {
        return CronTriggerBuilder.createCronTrigger(new CronTriggerBuilder.Builder().name(triggerName)
                .group(ScheduleJobType.TASK_QUEUE_INSERT.getTriggerGroup()).jobDataMap(jobDataMap)
                .hourAndMinuteOnGivenDaysOfWeek().hour(hour).minute(minute).daysOfWeek(daysOfWeek).build());
    }

    private Trigger createDailyTrigger(String triggerName, JobDataMap jobDataMap, int hour, int minute) {
        return CronTriggerBuilder.createCronTrigger(new CronTriggerBuilder.Builder().name(triggerName)
                .group(ScheduleJobType.TASK_QUEUE_INSERT.getTriggerGroup()).jobDataMap(jobDataMap).daily().hour(hour)
                .minute(minute).build());
    }

    private Trigger createHourlyTrigger(String triggerName, JobDataMap jobDataMap, int minute) {
        return SimpleTriggerBuilder.createSimpleTrigger(new SimpleTriggerBuilder.Builder().name(triggerName)
                .group(ScheduleJobType.TASK_QUEUE_INSERT.getTriggerGroup()).jobDataMap(jobDataMap).isRepeatForever(true)
                .minutes(minute).build());
    }
}
