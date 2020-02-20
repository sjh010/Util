package com.mobileleader.rpa.api.schedule.trigger;

import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class CronTriggerBuilder {

    private Trigger trigger;

    public static Trigger createCronTrigger(Builder builder) {
        return new CronTriggerBuilder(builder).getTrigger();
    }

    public Trigger getTrigger() {
        return trigger;
    }

    private CronTriggerBuilder(Builder builder) {
        TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(builder.name, builder.group).withSchedule(createScheduleBuilder(builder));
        if (builder.jobKey != null) {
            triggerBuilder = triggerBuilder.forJob(builder.jobKey);
        }
        if (builder.startDateTime != null) {
            triggerBuilder = triggerBuilder.startAt(builder.startDateTime.toDate());
        }
        if (builder.endDateTime != null) {
            triggerBuilder = triggerBuilder.endAt(builder.endDateTime.toDate());
        }
        if (builder.jobDataMap != null) {
            triggerBuilder = triggerBuilder.usingJobData(builder.jobDataMap);
        }
        trigger = triggerBuilder.build();
    }

    private CronScheduleBuilder createScheduleBuilder(Builder builder) {
        CronScheduleBuilder cronScheduleBuilder = null;
        if (builder.isCronExpression) {
            cronScheduleBuilder = CronScheduleBuilder.cronSchedule(builder.cronExpression);
        } else {
            if (builder.isDaily) {
                cronScheduleBuilder = CronScheduleBuilder.dailyAtHourAndMinute(builder.hour, builder.minute);
            } else if (builder.isWeekly) {
                cronScheduleBuilder = CronScheduleBuilder.weeklyOnDayAndHourAndMinute(builder.dayOfWeek, builder.hour,
                        builder.minute);
            } else if (builder.isMonthly) {
                cronScheduleBuilder = CronScheduleBuilder.monthlyOnDayAndHourAndMinute(builder.dayOfMonth, builder.hour,
                        builder.minute);
            } else if (builder.isHourAndMinuteOnGivenDaysOfWeek) {
                cronScheduleBuilder = CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek(builder.hour, builder.minute,
                        builder.daysOfWeek);
            }
        }
        return cronScheduleBuilder;
    }

    public static class Builder {
        private String name;

        private String group;

        private DateTime startDateTime;

        private DateTime endDateTime;

        private JobDataMap jobDataMap;

        private JobKey jobKey;

        private boolean isCronExpression = false;

        private String cronExpression;

        private int hour = 0;

        private int minute = 0;

        private Integer[] daysOfWeek;

        private Integer dayOfWeek;

        private int dayOfMonth;

        private boolean isDaily = false;

        private boolean isWeekly = false;

        private boolean isMonthly = false;

        private boolean isHourAndMinuteOnGivenDaysOfWeek = false;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder startDateTime(DateTime startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        public Builder endDateTime(DateTime endDateTime) {
            this.endDateTime = endDateTime;
            return this;
        }

        public Builder jobDataMap(JobDataMap jobDataMap) {
            this.jobDataMap = jobDataMap;
            return this;
        }

        public Builder jobKey(JobKey jobKey) {
            this.jobKey = jobKey;
            return this;
        }

        /**
         * Set cronExpression.
         *
         * @param cronExpression cronExpression
         * @return {@link Builder}
         */
        public Builder cronExpression(String cronExpression) {
            this.isCronExpression = true;
            this.isDaily = false;
            this.isWeekly = false;
            this.isMonthly = false;
            this.isHourAndMinuteOnGivenDaysOfWeek = false;
            this.cronExpression = cronExpression;
            return this;
        }

        /**
         * Set daily. required hour, minute.
         *
         * @return {@link Builder}
         */
        public Builder daily() {
            this.isCronExpression = false;
            this.isDaily = true;
            this.isWeekly = false;
            this.isMonthly = false;
            this.isHourAndMinuteOnGivenDaysOfWeek = false;
            return this;
        }

        /**
         * Set weekly. required hour, minute, dayOfWeek.
         *
         * @return {@link Builder}
         */
        public Builder weekly() {
            this.isCronExpression = false;
            this.isDaily = false;
            this.isWeekly = true;
            this.isMonthly = false;
            this.isHourAndMinuteOnGivenDaysOfWeek = false;
            return this;
        }

        /**
         * Set monthly. required hour, minute, dayOfMonth.
         *
         * @return {@link Builder}
         */
        public Builder monthly() {
            this.isCronExpression = false;
            this.isDaily = false;
            this.isWeekly = false;
            this.isMonthly = true;
            this.isHourAndMinuteOnGivenDaysOfWeek = false;
            return this;
        }

        /**
         * Set hourAndMinuteOnGivenDaysOfWeek. required hour, minute, daysOfWeek.
         *
         * @return {@link Builder}
         */
        public Builder hourAndMinuteOnGivenDaysOfWeek() {
            this.isCronExpression = false;
            this.isDaily = false;
            this.isWeekly = false;
            this.isMonthly = false;
            this.isHourAndMinuteOnGivenDaysOfWeek = true;
            return this;
        }

        public Builder dayOfMonth(int dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
            return this;
        }

        public Builder hour(int hour) {
            this.hour = hour;
            return this;
        }

        public Builder minute(int minute) {
            this.minute = minute;
            return this;
        }

        public Builder dayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public Builder daysOfWeek(Integer... daysOfWeek) {
            this.daysOfWeek = daysOfWeek;
            return this;
        }

        public Builder build() {
            return this;
        }
    }
}
