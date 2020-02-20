package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;

public class TaskQueueControlRequest {
    @NotNull
    private Integer workSequence;

    @NotNull
    private Integer robotSequence;

    public Integer getRobotSequence() {
        return robotSequence;
    }

    public void setRobotSequence(Integer robotSequence) {
        this.robotSequence = robotSequence;
    }

    public Integer getWorkSequence() {
        return workSequence;
    }

    public void setWorkSequence(Integer workSequence) {
        this.workSequence = workSequence;
    }
}
