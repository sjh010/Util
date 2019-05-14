package com.mobileleader.rpa.view.data.dto;

public class ProcessNameVersion {

    private Integer processVersionSequence;

    private Integer majorVersion;

    private Integer minorVersion;

    private Integer processSequence;

    private String processName;

    public ProcessNameVersion() {
        super();
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProcessNameVersion [processSequence=");
        builder.append(processSequence);
        builder.append(", processName=");
        builder.append(processName);
        builder.append(", processVersionSequence=");
        builder.append(processVersionSequence);
        builder.append(", majorVersion=");
        builder.append(majorVersion);
        builder.append(", minorVersion=");
        builder.append(minorVersion);
        builder.append("]");
        return builder.toString();
    }
}
