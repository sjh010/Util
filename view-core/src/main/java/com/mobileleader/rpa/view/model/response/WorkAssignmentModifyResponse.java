package com.mobileleader.rpa.view.model.response;

import java.util.List;



public class WorkAssignmentModifyResponse {

    private WorkAssignmentModifyErrorCode errorCode = WorkAssignmentModifyErrorCode.SUCCESS;

    private List<String> otherWorkAssignedRobotNameList;

    public enum WorkAssignmentModifyErrorCode {
        SUCCESS, ROBOT_IS_WORKING, ROBOT_IS_ASSIGNED;
    }

    public WorkAssignmentModifyResponse() {
        super();
    }

    public WorkAssignmentModifyErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(WorkAssignmentModifyErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getOtherWorkAssignedRobotNameList() {
        return otherWorkAssignedRobotNameList;
    }

    public void setOtherWorkAssignedRobotNameList(List<String> otherWorkAssignedRobotNameList) {
        this.otherWorkAssignedRobotNameList = otherWorkAssignedRobotNameList;
    }


}
