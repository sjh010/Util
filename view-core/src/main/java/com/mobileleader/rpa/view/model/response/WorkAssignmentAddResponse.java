package com.mobileleader.rpa.view.model.response;

import java.util.List;



public class WorkAssignmentAddResponse {

    private WorkAssignmentAddErrorCode errorCode = WorkAssignmentAddErrorCode.SUCCESS;

    private List<String> otherWorkAssignedRobotNameList;


    public enum WorkAssignmentAddErrorCode {
        SUCCESS, ROBOT_IS_ASSIGNED, PROCESS_IS_INACTIVE;
    }

    public WorkAssignmentAddResponse() {
        super();
    }

    public WorkAssignmentAddErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(WorkAssignmentAddErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getOtherWorkAssignedRobotNameList() {
        return otherWorkAssignedRobotNameList;
    }

    public void setOtherWorkAssignedRobotNameList(List<String> otherWorkAssignedRobotNameList) {
        this.otherWorkAssignedRobotNameList = otherWorkAssignedRobotNameList;
    }
}
