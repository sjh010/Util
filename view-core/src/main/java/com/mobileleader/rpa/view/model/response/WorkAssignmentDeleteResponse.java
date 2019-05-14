package com.mobileleader.rpa.view.model.response;

public class WorkAssignmentDeleteResponse {

    public enum WorkAssignmentDeleteResultCode {
        SUCCESS, ROBOT_IS_WORKING
    }

    private WorkAssignmentDeleteResultCode resultCode = WorkAssignmentDeleteResultCode.SUCCESS;

    public WorkAssignmentDeleteResponse() {
        super();
    }

    public WorkAssignmentDeleteResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(WorkAssignmentDeleteResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
