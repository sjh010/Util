package com.mobileleader.rpa.view.data.dto;

import com.mobileleader.rpa.data.dto.biz.TaskQueue;

public class TaskQueueByWork extends TaskQueue {

    private String robotName;

    private String taskStatusName;

    public TaskQueueByWork() {
        super();
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }
}
