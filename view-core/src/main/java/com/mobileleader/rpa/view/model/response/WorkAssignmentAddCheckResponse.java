package com.mobileleader.rpa.view.model.response;

public class WorkAssignmentAddCheckResponse {
    private WorkAssignmentAddCheckResultCode resultCode = WorkAssignmentAddCheckResultCode.SUCCESS;

    public enum WorkAssignmentAddCheckResultCode {
        SUCCESS, NO_ASSIGNALBE_PROCESS
    }

    public WorkAssignmentAddCheckResponse() {
        super();
    }

    public WorkAssignmentAddCheckResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(WorkAssignmentAddCheckResultCode resultCode) {
        this.resultCode = resultCode;
    }


}
