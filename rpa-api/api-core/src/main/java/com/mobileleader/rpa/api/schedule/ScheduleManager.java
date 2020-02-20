package com.mobileleader.rpa.api.schedule;

import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import com.mobileleader.rpa.api.schedule.job.ScheduleJobType;
import com.mobileleader.rpa.api.schedule.trigger.CronTriggerBuilder;
import com.mobileleader.rpa.api.schedule.trigger.JobTriggerInfo;
import com.mobileleader.rpa.api.schedule.trigger.SimpleTriggerBuilder;
import com.mobileleader.rpa.utils.json.JsonConverter;
import javax.annotation.PostConstruct;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ScheduleManager {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleManager.class);

    @Autowired
    private Scheduler scheduler;

    @Value("#{apiProperties['quartz.start.scheduler']}")
    private Boolean startingScheduler;

    /**
     * Post Constructor.
     */
    @PostConstruct
    public void initialize() {
        if (startingScheduler != null && startingScheduler) {
            try {
                addDefaultJobDetails();
                addDefaultTriggers();
                scheduler.start();
            } catch (SchedulerException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void addDefaultJobDetails() throws SchedulerException {
        scheduler.addJob(ScheduleJobType.TEMP_AUTHENTICATION_DELETE.getJobDetail("DEFAULT", true), true);
        scheduler.addJob(ScheduleJobType.STATISTICS_TASK_QUEUE_DELETE.getJobDetail("DEFAULT", true), true);
    }

    private void addDefaultTriggers() throws SchedulerException {
        ScheduleJobType tempAuthenticationDeleteJob = ScheduleJobType.TEMP_AUTHENTICATION_DELETE;
        addTrigger(SimpleTriggerBuilder.createSimpleTrigger(
                new SimpleTriggerBuilder.Builder().jobKey(tempAuthenticationDeleteJob.getJobKey("DEFAULT"))
                        .name(tempAuthenticationDeleteJob.getTriggerNamePrefix() + "DEFAULT")
                        .group(tempAuthenticationDeleteJob.getTriggerGroup()).isRepeatForever(true).hours(1).build()));

        ScheduleJobType statisticsTaskQueueDeleteJob = ScheduleJobType.STATISTICS_TASK_QUEUE_DELETE;
        addTrigger(CronTriggerBuilder.createCronTrigger(
                new CronTriggerBuilder.Builder().jobKey(statisticsTaskQueueDeleteJob.getJobKey("DEFAULT"))
                        .name(statisticsTaskQueueDeleteJob.getTriggerNamePrefix() + "DEFAULT")
                        .group(statisticsTaskQueueDeleteJob.getTriggerGroup()).daily().hour(1).minute(0).build()));
    }

    /**
     * 스케줄을 등록한다.
     *
     * @param jobDetail {@link JobDetail}
     * @param trigger {@link Trigger}
     * @return boolean
     */
    public boolean addSchedule(JobDetail jobDetail, Trigger trigger) {
        boolean isAdded = false;
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            isAdded = true;
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
        return isAdded;
    }

    /**
     * Trigger를 추가한다.
     *
     * @param trigger {@link Trigger}
     * @return boolean isAdded
     */
    public boolean addTrigger(Trigger trigger) {
        boolean isAdded = false;
        try {
            removeTrigger(trigger.getKey());
            scheduler.scheduleJob(trigger);
            isAdded = true;
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
        return isAdded;
    }

    /**
     * 기존 스케줄이 존재하면 재등록한다. 없으면 신규 등록.
     *
     * @param trigger {@link Trigger}
     * @return boolean
     */
    public boolean rescheduleTrigger(Trigger trigger) {
        try {
            if (checkExists(trigger.getKey())) {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            } else {
                addTrigger(trigger);
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
        return true;
    }

    /**
     * Trigger를 재개한다.
     *
     * @param triggerName triggerName
     * @param triggerGroup triggerGroup
     */
    public void resumeTrigger(String triggerName, String triggerGroup) {
        resumeTrigger(new TriggerKey(triggerName, triggerGroup));
    }

    /**
     * Trigger를 재개한다.
     *
     * @param triggerKey {@link TriggerKey}
     */
    public void resumeTrigger(TriggerKey triggerKey) {
        try {
            scheduler.resumeTrigger(triggerKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
    }

    /**
     * Trigger를 정지한다.
     *
     * @param triggerName triggerName
     * @param triggerGroup triggerGroup
     */
    public void pauseTrigger(String triggerName, String triggerGroup) {
        pauseTrigger(new TriggerKey(triggerName, triggerGroup));
    }

    /**
     * Trigger를 정지한다.
     *
     * @param triggerKey {@link TriggerKey}
     */
    public void pauseTrigger(TriggerKey triggerKey) {
        try {
            if (checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
    }

    /**
     * 등록된 Trigger를 삭제한다.
     *
     * @param triggerName triggerName
     * @param triggerGroup triggerGroup
     * @return boolean
     */
    public boolean removeTrigger(String triggerName, String triggerGroup) {
        return removeTrigger(new TriggerKey(triggerName, triggerGroup));
    }


    /**
     * 등록된 Trigger를 삭제한다.
     *
     * @param triggerKey {@link TriggerKey}
     * @return boolean
     */
    public boolean removeTrigger(TriggerKey triggerKey) {
        try {
            if (scheduler.checkExists(triggerKey)) {
                return scheduler.unscheduleJob(triggerKey);
            } else {
                return false;
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
    }

    public boolean deleteJob(String jobName, String jobGroup) {
        return deleteJob(new JobKey(jobName, jobGroup));
    }

    /**
     * Job을 삭제한다. 존재하지 않는 Job을 삭제 시도 하는 것도 true 리턴.
     *
     * @param jobKey {@link JobKey}
     * @return boolean
     */
    public boolean deleteJob(JobKey jobKey) {
        boolean isSuccess = true;
        try {
            if (scheduler.checkExists(jobKey)) {
                isSuccess = scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
        return isSuccess;
    }

    /**
     * 트리거 존재 여부 확인.
     *
     * @param triggerName 트리거명
     * @param triggerGroup 트리거그룹
     * @return boolean
     */
    public boolean checkExists(String triggerName, String triggerGroup) {
        return checkExists(new TriggerKey(triggerName, triggerGroup));
    }

    /**
     * 트리거 존재 여부 확인.
     *
     * @param triggerKey {@link TriggerKey}
     * @return boolean
     */
    public boolean checkExists(TriggerKey triggerKey) {
        try {
            if (scheduler.checkExists(triggerKey)) {
                return true;
            } else {
                return false;
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
    }

    /**
     * 트리거를 조회한다.
     *
     * @param triggerName 트리거명
     * @param triggerGroup 트리거그룹
     * @return {@link Trigger}
     */
    public Trigger getTrigger(String triggerName, String triggerGroup) {
        return getTrigger(new TriggerKey(triggerName, triggerGroup));
    }

    /**
     * 트리거를 조회한다.
     *
     * @param triggerKey {@link TriggerKey}
     * @return {@link Trigger}
     */
    public Trigger getTrigger(TriggerKey triggerKey) {
        try {
            return scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
    }

    /**
     * 트리거 정보를 조회한다.
     *
     * @param name triggerName
     * @param group triggerGroup
     * @return {@link JobTriggerInfo}
     * @throws SchedulerException schedulerException
     */
    public JobTriggerInfo getJobTriggerInfo(String name, String group) {
        try {
            showSchedulerMetaData();
            showTriggerState(name, group);

            TriggerKey triggerKey = new TriggerKey(name, group);
            if (!scheduler.checkExists(triggerKey)) {
                return null;
            }
            Trigger trigger = scheduler.getTrigger(triggerKey);
            return new JobTriggerInfo.Builder().triggerKey(trigger.getKey()).startTime(trigger.getStartTime())
                    .endTime(trigger.getEndTime()).nextFireTime(trigger.getNextFireTime())
                    .previousFireTime(trigger.getPreviousFireTime()).finalFireTime(trigger.getFinalFireTime()).build();
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
    }

    /**
     * 스케줄러 정보를 표시한다.
     */
    public void showSchedulerMetaData() {
        try {
            logger.info(scheduler.getMetaData().getSummary());
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
    }

    /**
     * 트리거 상태를 표시한다.
     *
     * @param name triggerName
     * @param group triggerGroup
     */
    public void showTriggerState(String name, String group) {
        try {
            logger.info(JsonConverter.toPrettyJson(scheduler.getTriggerState(new TriggerKey(name, group))));
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RpaApiException(RpaApiError.SCHEDULE_ERROR, e);
        }
    }
}
