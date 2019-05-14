package com.mobileleader.rpa.view.data.dto;

public class WorkModify {

    private Integer workSequence;

    private String repeatCycleUnitCode;

    private Integer repeatCycle;

    private String executeStandardValue = null;

    private String executeHourminute = null;

    private String activationYn;

    private String modifyId;

    public WorkModify() {
        super();
    }

    public Integer getWorkSequence() {
        return workSequence;
    }

    public void setWorkSequence(Integer workSequence) {
        this.workSequence = workSequence;
    }

    public String getRepeatCycleUnitCode() {
        return repeatCycleUnitCode;
    }

    public void setRepeatCycleUnitCode(String repeatCycleUnitCode) {
        this.repeatCycleUnitCode = repeatCycleUnitCode;
    }

    public Integer getRepeatCycle() {
        return repeatCycle;
    }

    public void setRepeatCycle(Integer repeatCycle) {
        this.repeatCycle = repeatCycle;
    }

    public String getExecuteStandardValue() {
        return executeStandardValue;
    }

    public void setExecuteStandardValue(String executeStandardValue) {
        this.executeStandardValue = executeStandardValue;
    }

    public String getExecuteHourminute() {
        return executeHourminute;
    }

    public void setExecuteHourminute(String executeHourminute) {
        this.executeHourminute = executeHourminute;
    }

    public String getActivationYn() {
        return activationYn;
    }

    public void setActivationYn(String activationYn) {
        this.activationYn = activationYn;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }


}
