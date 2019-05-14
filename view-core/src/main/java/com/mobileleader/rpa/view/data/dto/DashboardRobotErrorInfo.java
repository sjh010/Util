package com.mobileleader.rpa.view.data.dto;

public class DashboardRobotErrorInfo {

    private String hour;

    private Integer count = 0;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
