package com.mobileleader.rpa.view.data.dto;

public class DashboardProcessExecuteInfo {

    private Integer processVersionSequence = 0;

    private String processName;

    private Integer majorVersion = 0;

    private Integer minorVersion = 0;

    private Integer executeCount = 0;

    public String getProcessName() {
        return processName;
    }

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public void setProcessVersionSequence(Integer processVersionSequence) {
        this.processVersionSequence = processVersionSequence;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(Integer majorVersion) {
        this.majorVersion = majorVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

}
