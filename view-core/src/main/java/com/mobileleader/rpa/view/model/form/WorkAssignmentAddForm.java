package com.mobileleader.rpa.view.model.form;

import java.util.List;

public class WorkAssignmentAddForm {

    private List<Integer> robotSequenceList;

    private Integer processVersionSequence;

    private String repeatCycleUnitCd;

    private String executeStandardValue;

    private String execHourMinute;

    private String workActiveYn;

    public WorkAssignmentAddForm() {
        super();
    }

    public List<Integer> getRobotSequenceList() {
        return robotSequenceList;
    }

    public void setRobotSequenceList(List<Integer> robotSequenceList) {
        this.robotSequenceList = robotSequenceList;
    }

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public void setProcessVersionSequence(Integer processVersionSequence) {
        this.processVersionSequence = processVersionSequence;
    }

    public String getRepeatCycleUnitCd() {
        return repeatCycleUnitCd;
    }

    public void setRepeatCycleUnitCd(String repeatCycleUnitCd) {
        this.repeatCycleUnitCd = repeatCycleUnitCd;
    }

    public String getExecuteStandardValue() {
        return executeStandardValue;
    }

    public void setExecuteStandardValue(String executeStandardValue) {
        this.executeStandardValue = executeStandardValue;
    }

    public String getExecHourMinute() {
        return execHourMinute;
    }

    public void setExecHourMinute(String execHourMinute) {
        this.execHourMinute = execHourMinute;
    }

    public String getWorkActiveYn() {
        return workActiveYn;
    }

    public void setWorkActiveYn(String workActiveYn) {
        this.workActiveYn = workActiveYn;
    }
}
