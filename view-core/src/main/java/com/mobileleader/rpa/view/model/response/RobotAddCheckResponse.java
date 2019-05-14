package com.mobileleader.rpa.view.model.response;

public class RobotAddCheckResponse {

    private boolean robotExistYn;

    private boolean pcExistYn;

    public boolean isRobotExistYn() {
        return robotExistYn;
    }

    public void setRobotExistYn(boolean robotExistYn) {
        this.robotExistYn = robotExistYn;
    }

    public boolean isPcExistYn() {
        return pcExistYn;
    }

    public void setPcExistYn(boolean pcExistYn) {
        this.pcExistYn = pcExistYn;
    }

}
