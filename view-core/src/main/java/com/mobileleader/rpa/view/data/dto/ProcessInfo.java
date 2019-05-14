package com.mobileleader.rpa.view.data.dto;

import org.joda.time.DateTime;

public class ProcessInfo {

    private Integer processSequence;

    private String processName;

    private Integer processVersionSequence;

    private String processVersion;

    private Integer majorVersion;

    private Integer minorVersion;

    private String registerId;

    private DateTime registerDateTime;

    private String configManagementStatusCode;

    private String configManagementStatusModifyDateTime;

    private String configManagementUserId;

    private String activationYn;

    private String activationYnName;

    private String workAssignmentYn;

    private String workAssignmentYnName;

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

    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
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

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getConfigManagementStatusCode() {
        return configManagementStatusCode;
    }

    public void setConfigManagementStatusCode(String configManagementStatusCode) {
        this.configManagementStatusCode = configManagementStatusCode;
    }

    public DateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(DateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public String getConfigManagementStatusModifyDateTime() {
        return configManagementStatusModifyDateTime;
    }

    public void setConfigManagementStatusModifyDateTime(String configManagementStatusModifyDateTime) {
        this.configManagementStatusModifyDateTime = configManagementStatusModifyDateTime;
    }

    public String getConfigManagementUserId() {
        return configManagementUserId;
    }

    public void setConfigManagementUserId(String configManagementUserId) {
        this.configManagementUserId = configManagementUserId;
    }

    public String getActivationYn() {
        return activationYn;
    }

    public void setActivationYn(String activationYn) {
        this.activationYn = activationYn;
    }

    public String getActivationYnName() {
        return activationYnName;
    }

    public void setActivationYnName(String activationYnName) {
        this.activationYnName = activationYnName;
    }

    public String getWorkAssignmentYn() {
        return workAssignmentYn;
    }

    public void setWorkAssignmentYn(String workAssignmentYn) {
        this.workAssignmentYn = workAssignmentYn;
    }

    public String getWorkAssignmentYnName() {
        return workAssignmentYnName;
    }

    public void setWorkAssignmentYnName(String workAssignmentYnName) {
        this.workAssignmentYnName = workAssignmentYnName;
    }

}
