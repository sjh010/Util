package com.mobileleader.rpa.view.rest.model;

import javax.validation.constraints.NotNull;

public class WeeklyScheduleRequest extends TaskQueueScheduleRequest {

    @NotNull
    private Integer[] daysOfWeek;

    private int hour;

    private int minute;

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public WeeklyScheduleRequest(Builder builder) {
        super.setRobotSequence(builder.robotSequence);
        super.setWorkSequence(builder.workSequence);
        super.setProcessVersionSequence(builder.processVersionSequence);
        super.setRepeatCount(builder.repeatCount);
        minute = builder.minute;
        hour = builder.hour;
        daysOfWeek = builder.daysOfWeek;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public Integer[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public static class Builder {
        private Integer workSequence;

        private Integer processVersionSequence;

        private Integer robotSequence;

        private int repeatCount = -1;

        private int minute;

        private int hour;

        private Integer[] daysOfWeek;

        public Builder robotSequence(Integer robotSequence) {
            this.robotSequence = robotSequence;
            return this;
        }

        public Builder workSequence(Integer workSequence) {
            this.workSequence = workSequence;
            return this;
        }

        public Builder processVersionSequence(Integer processVersionSequence) {
            this.processVersionSequence = processVersionSequence;
            return this;
        }

        public Builder repeatCount(int repeatCount) {
            this.repeatCount = repeatCount;
            return this;
        }

        /**
         * parse hour and minute.
         *
         * @param hourMinutes HHmm
         * @return this
         */
        public Builder hourMinutes(String hourMinutes) {
            this.hour = Integer.parseInt(hourMinutes.substring(0, 2));
            this.minute = Integer.parseInt(hourMinutes.substring(2, 4));
            return this;
        }

        public Builder daysOfWeek(Integer[] daysOfWeek) {
            this.daysOfWeek = daysOfWeek;
            return this;
        }

        public WeeklyScheduleRequest build() {
            return new WeeklyScheduleRequest(this);
        }
    }
}
