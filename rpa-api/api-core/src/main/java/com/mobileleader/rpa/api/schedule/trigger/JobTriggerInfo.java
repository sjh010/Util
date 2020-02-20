package com.mobileleader.rpa.api.schedule.trigger;

import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import java.util.Date;
import org.joda.time.DateTime;
import org.quartz.TriggerKey;

public class JobTriggerInfo {

    private TriggerKey triggerKey;

    private DateTime startTime;

    private DateTime endTime;

    private DateTime previousFireTime;

    private DateTime nextFireTime;

    private DateTime finalFireTime;

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public JobTriggerInfo(Builder builder) {
        triggerKey = builder.triggerKey;
        startTime = builder.startTime;
        endTime = builder.endTime;
        previousFireTime = builder.previousFireTime;
        nextFireTime = builder.nextFireTime;
        finalFireTime = builder.finalFireTime;
    }

    public TriggerKey getTriggerKey() {
        return triggerKey;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public DateTime getPreviousFireTime() {
        return previousFireTime;
    }

    public DateTime getNextFireTime() {
        return nextFireTime;
    }

    public DateTime getFinalFireTime() {
        return finalFireTime;
    }

    public static class Builder {
        private TriggerKey triggerKey;

        private DateTime startTime;

        private DateTime endTime;

        private DateTime previousFireTime;

        private DateTime nextFireTime;

        private DateTime finalFireTime;

        public Builder triggerKey(TriggerKey triggerKey) {
            this.triggerKey = triggerKey;
            return this;
        }

        public Builder startTime(Date startTime) {
            this.startTime = DateTimeUtils.convertFromDate(startTime);
            return this;
        }

        public Builder endTime(Date endTime) {
            this.endTime = DateTimeUtils.convertFromDate(endTime);
            return this;
        }

        public Builder previousFireTime(Date previousFireTime) {
            this.previousFireTime = DateTimeUtils.convertFromDate(previousFireTime);
            return this;
        }

        public Builder nextFireTime(Date nextFireTime) {
            this.nextFireTime = DateTimeUtils.convertFromDate(nextFireTime);
            return this;
        }

        public Builder finalFireTime(Date finalFireTime) {
            this.finalFireTime = DateTimeUtils.convertFromDate(finalFireTime);
            return this;
        }

        public JobTriggerInfo build() {
            return new JobTriggerInfo(this);
        }
    }
}
