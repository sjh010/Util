package com.mobileleader.rpa.view.data.dto;

public class WorkRegist {
    private Integer workSequence;

    private Integer processVersionSequence;

    private String repeatCycleUnitCode;

    private Integer repeatCycle;

    private String executeStandardValue = null;

    private String executeHourminute = null;

    private String activationYn;

    private String registerId;

    public WorkRegist() {
        super();
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

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
}
