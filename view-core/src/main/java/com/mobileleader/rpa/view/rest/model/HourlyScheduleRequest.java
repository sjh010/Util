package com.mobileleader.rpa.view.rest.model;

public class HourlyScheduleRequest extends TaskQueueScheduleRequest {

    private int minute;

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public HourlyScheduleRequest(Builder builder) {
        super.setRobotSequence(builder.robotSequence);
        super.setWorkSequence(builder.workSequence);
        super.setProcessVersionSequence(builder.processVersionSequence);
        super.setRepeatCount(builder.repeatCount);
        minute = builder.minute;
    }

    public int getMinute() {
        return minute;
    }

    public static class Builder {
        private Integer workSequence;

        private Integer processVersionSequence;

        private Integer robotSequence;

        private int repeatCount = -1;

        private int minute;

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

        public Builder minute(String minute) {
            this.minute = Integer.parseInt(minute);
            return this;
        }

        public HourlyScheduleRequest build() {
            return new HourlyScheduleRequest(this);
        }
    }
}
