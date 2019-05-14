package com.mobileleader.rpa.view.data.dto;

/**
 * 업무에 할당된 로봇.
 *
 * @author jkkim
 *
 */
public class WorkAssignedRobot {

    private Integer robotSequence;
    private String robotName;

    /**
     * 로봇이 다른업무에 할당된 개수.
     */
    private Integer workAssignedCount;

    public WorkAssignedRobot() {
        super();
    }

    public Integer getRobotSequence() {
        return robotSequence;
    }

    public void setRobotSequence(Integer robotSequence) {
        this.robotSequence = robotSequence;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public Integer getWorkAssignedCount() {
        return workAssignedCount;
    }

    public void setWorkAssignedCount(Integer workAssignedCount) {
        this.workAssignedCount = workAssignedCount;
    }
}
