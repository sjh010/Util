package com.mobileleader.rpa.view.model.form;

public class RobotLogSearchForm extends BaseSearchForm {

    private Integer robotSequence;

    private String robotName;

    private String robotLogTypeCode;

    private String robotLogStatusCode;

    private String registerDateTime;

    public Integer getRobotSequence() {
        return robotSequence;
    }

    public void setRobotSequence(Integer robotSequence) {
        this.robotSequence = robotSequence;
    }

    public String getRobotLogStatusCode() {
        return robotLogStatusCode;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getRobotLogTypeCode() {
        return robotLogTypeCode;
    }

    public void setRobotLogTypeCode(String robotLogTypeCode) {
        this.robotLogTypeCode = robotLogTypeCode;
    }

    public void setRobotLogStatusCode(String robotLogStatusCode) {
        this.robotLogStatusCode = robotLogStatusCode;
    }

    public String getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(String registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RobotLogSearchForm [");
        if (robotSequence != null) {
            builder.append("robotSequence=").append(robotSequence).append(", ");
        }
        if (robotName != null) {
            builder.append("robotName=").append(robotName).append(", ");
        }
        if (robotLogTypeCode != null) {
            builder.append("robotLogTypeCode=").append(robotLogTypeCode).append(", ");
        }
        if (robotLogStatusCode != null) {
            builder.append("robotLogStatusCode=").append(robotLogStatusCode).append(", ");
        }
        if (registerDateTime != null) {
            builder.append("registerDateTime=").append(registerDateTime);
        }
        builder.append("]");
        return builder.toString();
    }

}
