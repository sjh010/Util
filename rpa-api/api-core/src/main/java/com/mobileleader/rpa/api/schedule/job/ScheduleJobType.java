package com.mobileleader.rpa.api.schedule.job;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

public enum ScheduleJobType {

    // @formatter:off
    TASK_QUEUE_INSERT(TaskQueueInsertJob.class,
            "TASK_QUE_INS_JOB_GRP", "TASK_QUE_INS_JOB_",
                "TASK_QUE_INS_TRIGGER_GRP", "TASK_QUE_INS_TRIGGER_"),
    ROBOT_STATUS_UPDATE(RobotStatusUpdateJob.class,
            "RBT_STAT_UPDATE_JOB_GRP", "RBT_STAT_UPDATE_JOB_",
                "RBT_STAT_UPDATE_TRIGGER_GRP", "RBT_STAT_UPDATE_TRIGGER_"),
    TEMP_AUTHENTICATION_DELETE(TempAuthenticationDeleteJob.class,
            "TMP_AUTH_DEL_JOB_GRP", "TMP_AUTH_DEL_JOB_",
                "TMP_AUTH_DEL_TRIGGER_GRP", "TMP_AUTH_DEL_TRIGGER_"),
    STATISTICS_TASK_QUEUE_DELETE(StatisticsTaskQueueDeleteJob.class,
            "STAT_TASK_QUE_DEL_JOB_GRP", "STAT_TASK_QUE_DEL_JOB_",
                "STAT_TASK_QUE_DEL_TRIGGER_GRP", "STAT_TASK_QUE_DEL_TRIGGER_");
    // @formatter:on

    private final Class<? extends Job> jobClazz;

    private final String jobGroup;

    private final String jobNamePrefix;

    private final String triggerGroup;

    private final String triggerNamePrefix;

    private ScheduleJobType(Class<? extends Job> jobClazz, String jobGroup, String jobNamePrefix, String triggerGroup,
            String triggerNamePrefix) {
        this.jobClazz = jobClazz;
        this.jobGroup = jobGroup;
        this.jobNamePrefix = jobNamePrefix;
        this.triggerGroup = triggerGroup;
        this.triggerNamePrefix = triggerNamePrefix;
    }

    public JobDetail getJobDetail(String jobName) {
        return getJobDetail(jobName, false);
    }

    public JobDetail getJobDetail(String jobName, boolean storeDurably) {
        return JobBuilder.newJob().ofType(jobClazz).withIdentity(this.jobNamePrefix + jobName, this.jobGroup)
                .storeDurably(storeDurably).build();
    }

    public Class<? extends Job> getJobClass() {
        return jobClazz;
    }

    public JobKey getJobKey(String jobName) {
        return new JobKey(this.jobNamePrefix + jobName, this.jobGroup);
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public String getJobNamePrefix() {
        return jobNamePrefix;
    }

    public TriggerKey getTriggerKey(String triggerName) {
        return new TriggerKey(this.triggerNamePrefix + triggerName, this.triggerGroup);
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public String getTriggerNamePrefix() {
        return triggerNamePrefix;
    }
}
