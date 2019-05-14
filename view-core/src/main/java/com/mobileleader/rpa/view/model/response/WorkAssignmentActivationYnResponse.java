package com.mobileleader.rpa.view.model.response;

public class WorkAssignmentActivationYnResponse {

    // 이미 활성된 업무 수
    int workActivationYCount = 0;

    // 이미 비활성된 업무 수
    int workActivationNCount = 0;

    // 활성 상태가 변경된 업무 수
    int changedCount = 0;

    private WorkAssignmentActivationYnErrorCode errorCode = WorkAssignmentActivationYnErrorCode.SUCCESS;

    private WorkAssignmentActivationYnResultCode resultCode = WorkAssignmentActivationYnResultCode.SUCCESS;

    public enum WorkAssignmentActivationYnErrorCode {
        SUCCESS, ROBOT_IS_WORKING, ROBOT_IS_ASSIGNED
    }

    public enum WorkAssignmentActivationYnResultCode {
        SUCCESS, ACTIVATE_ONLY_INACTIVED, INACTIVATE_ONLY_ACTIVED
    }

    public WorkAssignmentActivationYnResponse() {
        super();
    }

    public WorkAssignmentActivationYnErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(WorkAssignmentActivationYnErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public WorkAssignmentActivationYnResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(WorkAssignmentActivationYnResultCode resultCode) {
        this.resultCode = resultCode;
    }


    public int getWorkActivationYCount() {
        return workActivationYCount;
    }

    public void setWorkActivationYCount(int workActivationYCount) {
        this.workActivationYCount = workActivationYCount;
    }

    public int getWorkActivationNCount() {
        return workActivationNCount;
    }

    public void setWorkActivationNCount(int workActivationNCount) {
        this.workActivationNCount = workActivationNCount;
    }

    public int getChangedCount() {
        return changedCount;
    }

    public void setChangedCount(int changedCount) {
        this.changedCount = changedCount;
    }

}
