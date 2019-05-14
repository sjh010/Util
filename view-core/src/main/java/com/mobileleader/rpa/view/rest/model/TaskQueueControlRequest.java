package com.mobileleader.rpa.view.rest.model;

public class TaskQueueControlRequest {
    private Integer workSequence;

    private Integer robotSequence;

    public TaskQueueControlRequest(Builder builder) {
        workSequence = builder.workSequence;
        robotSequence = builder.robotSequence;
    }

    public Integer getRobotSequence() {
        return robotSequence;
    }

    public Integer getWorkSequence() {
        return workSequence;
    }

    public static class Builder {
        private Integer workSequence;

        private Integer robotSequence;

        public Builder robotSequence(Integer robotSequence) {
            this.robotSequence = robotSequence;
            return this;
        }

        public Builder workSequence(Integer workSequence) {
            this.workSequence = workSequence;
            return this;
        }

        public TaskQueueControlRequest build() {
            return new TaskQueueControlRequest(this);
        }
    }
}
