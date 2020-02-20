package com.mobileleader.rpa.api.schedule.trigger;

import org.joda.time.DateTime;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class SimpleTriggerBuilder {

    private Trigger trigger;

    public static Trigger createSimpleTrigger(Builder builder) {
        return new SimpleTriggerBuilder(builder).getTrigger();
    }

    public Trigger getTrigger() {
        return trigger;
    }

    private SimpleTriggerBuilder(Builder builder) {
        TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder.newTrigger()
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

    private SimpleScheduleBuilder createScheduleBuilder(Builder builder) {
        SimpleScheduleBuilder scheduleBuilder = null;
        if (builder.isHourly) {
            if (builder.isRepeatForever) {
                scheduleBuilder = SimpleScheduleBuilder.repeatHourlyForever(builder.hours);
            } else if (builder.hours < 0) {
                scheduleBuilder = SimpleScheduleBuilder.repeatHourlyForTotalCount(builder.repeatCount);
            } else {
                scheduleBuilder = SimpleScheduleBuilder.repeatHourlyForTotalCount(builder.repeatCount, builder.hours);
            }
        } else if (builder.isMinutely) {
            if (builder.isRepeatForever) {
                scheduleBuilder = SimpleScheduleBuilder.repeatMinutelyForever(builder.minutes);
            } else if (builder.minutes < 0) {
                scheduleBuilder = SimpleScheduleBuilder.repeatMinutelyForTotalCount(builder.repeatCount);
            } else {
                scheduleBuilder =
                        SimpleScheduleBuilder.repeatMinutelyForTotalCount(builder.repeatCount, builder.minutes);
            }
        } else if (builder.isSecondly) {
            if (builder.isRepeatForever) {
                scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(builder.seconds);
            } else if (builder.seconds < 0) {
                scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(builder.repeatCount);
            } else {
                scheduleBuilder =
                        SimpleScheduleBuilder.repeatSecondlyForTotalCount(builder.repeatCount, builder.seconds);
            }
        }
        return scheduleBuilder;
    }

    public static class Builder {
        private String name;

        private String group;

        private DateTime startDateTime;

        private DateTime endDateTime;

        private JobDataMap jobDataMap;

        private JobKey jobKey;

        private boolean isHourly = false;

        private int hours = 0;

        private boolean isMinutely = false;

        private int minutes = 0;

        private boolean isSecondly = false;

        private int seconds = 0;

        private boolean isRepeatForever = false;

        private int repeatCount = 0;

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
         * Set hours.
         *
         * @param hours hours
         * @return {@link Builder}
         */
        public Builder hours(int hours) {
            this.isHourly = true;
            this.isMinutely = false;
            this.isSecondly = false;
            this.hours = hours;
            return this;
        }

        /**
         * Set minutes.
         *
         * @param minutes minutes
         * @return {@link Builder}
         */
        public Builder minutes(int minutes) {
            this.isHourly = false;
            this.isMinutely = true;
            this.isSecondly = false;
            this.minutes = minutes;
            return this;
        }

        /**
         * Set seconds.
         *
         * @param seconds seconds
         * @return {@link Builder}
         */
        public Builder seconds(int seconds) {
            this.isHourly = false;
            this.isMinutely = false;
            this.isSecondly = true;
            this.seconds = seconds;
            return this;
        }

        /**
         * Set repeat forever.
         *
         * @param isRepeatForever repeatForever
         * @return {@link Builder}
         */
        public Builder isRepeatForever(boolean isRepeatForever) {
            this.repeatCount = 0;
            this.isRepeatForever = isRepeatForever;
            return this;
        }

        /**
         * Set repeat count.
         *
         * @param repeatCount repeatCount
         * @return {@link Builder}
         */
        public Builder repeatCount(int repeatCount) {
            this.isRepeatForever = false;
            this.repeatCount = repeatCount;
            return this;
        }

        public Builder build() {
            return this;
        }
    }
}
