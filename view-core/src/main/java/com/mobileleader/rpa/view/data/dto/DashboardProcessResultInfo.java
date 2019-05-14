package com.mobileleader.rpa.view.data.dto;

public class DashboardProcessResultInfo {

    private String taskStatusCode;

    private Integer count = 0;

    public String getTaskStatusCode() {
        return taskStatusCode;
    }

    public void setTaskStatusCode(String taskStatusCode) {
        this.taskStatusCode = taskStatusCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
