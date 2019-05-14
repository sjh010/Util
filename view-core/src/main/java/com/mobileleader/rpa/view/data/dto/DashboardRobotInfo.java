package com.mobileleader.rpa.view.data.dto;

public class DashboardRobotInfo {

    private Integer totalCount = 0;

    private Integer disconnectCount = 0;

    private Integer stopCount = 0;

    private Integer waitCount = 0;

    private Integer workCount = 0;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getDisconnectCount() {
        return disconnectCount;
    }

    public void setDisconnectCount(Integer disconnectCount) {
        this.disconnectCount = disconnectCount;
    }

    public Integer getStopCount() {
        return stopCount;
    }

    public void setStopCount(Integer stopCount) {
        this.stopCount = stopCount;
    }

    public Integer getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(Integer waitCount) {
        this.waitCount = waitCount;
    }

    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

}
