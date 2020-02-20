package com.mobileleader.rpa.api.schedule.job;

import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.springframework.util.Assert;

public abstract class AbstractRpaJob implements Job {

    @SuppressWarnings("unchecked")
    protected <T> T getTriggerJobData(JobExecutionContext context, String jobDataKey) {
        Assert.notNull(jobDataKey, "jobDataKey must not be null");
        JobDataMap triggerJobDataMap = getTriggerJobDataMap(context);
        return triggerJobDataMap != null ? (T) triggerJobDataMap.get(jobDataKey) : null;
    }

    protected JobDataMap getTriggerJobDataMap(JobExecutionContext context) {
        Trigger trigger = getTrigger(context);
        return trigger != null ? trigger.getJobDataMap() : null;
    }

    protected Trigger getTrigger(JobExecutionContext context) {
        return context.getTrigger() != null ? context.getTrigger() : null;
    }

    protected DateTime getNextFireTime(JobExecutionContext context) {
        return DateTimeUtils.convertFromDate(context.getNextFireTime());
    }

    protected DateTime getFireTime(JobExecutionContext context) {
        return DateTimeUtils.convertFromDate(context.getFireTime());
    }
}
