package com.mobileleader.rpa.view.data.dto;

import org.joda.time.DateTime;

public class WorkAssignmentList {

    private Integer workSequence;

    private Integer processSequence;

    private String processName;

    private Integer processVersionSequence;

    private String majorVersion;

    private Integer minorVersion;

    private String repeatCycleUnitCode;

    private Integer repeatCycle;

    private String executeStandardValue;

    private String executeHourminute;

    private String activationYn;

    private DateTime registerDateTime;

    private String registerId;

    private Integer robotCount;

    public Integer getWorkSequence() {
        return workSequence;
    }

    public void setWorkSequence(Integer workSequence) {
        this.workSequence = workSequence;
    }

    public Integer getProcessSequence() {
        return processSequence;
    }

    public void setProcessSequence(Integer processSequence) {
        this.processSequence = processSequence;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public void setProcessVersionSequence(Integer processVersionSequence) {
        this.processVersionSequence = processVersionSequence;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
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

    public DateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(DateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public Integer getRobotCount() {
        return robotCount;
    }

    public void setRobotCount(Integer robotCount) {
        this.robotCount = robotCount;
    }

}
