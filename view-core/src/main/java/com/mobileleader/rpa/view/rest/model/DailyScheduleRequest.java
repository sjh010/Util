package com.mobileleader.rpa.view.rest.model;

public class DailyScheduleRequest extends TaskQueueScheduleRequest {

    private int hour;

    private int minute;

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public DailyScheduleRequest(Builder builder) {
        super.setRobotSequence(builder.robotSequence);
        super.setWorkSequence(builder.workSequence);
        super.setProcessVersionSequence(builder.processVersionSequence);
        super.setRepeatCount(builder.repeatCount);
        minute = builder.minute;
        hour = builder.hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public static class Builder {
        private Integer workSequence;

        private Integer processVersionSequence;

        private Integer robotSequence;

        private int repeatCount = -1;

        private int minute;

        private int hour;

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

        public DailyScheduleRequest build() {
            return new DailyScheduleRequest(this);
        }
    }
}
