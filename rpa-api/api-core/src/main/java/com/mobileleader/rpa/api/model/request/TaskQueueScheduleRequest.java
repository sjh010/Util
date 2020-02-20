package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;

public class TaskQueueScheduleRequest {

    @NotNull
    private Integer workSequence;

    @NotNull
    private Integer processVersionSequence;

    @NotNull
    private Integer robotSequence;

    private int repeatCount = -1;

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

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public void setProcessVersionSequence(Integer processVersionSequence) {
        this.processVersionSequence = processVersionSequence;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }
}
