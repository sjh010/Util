package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.RobotName;
import java.util.List;

public class TaskQueueFileterResponse {

    List<RobotName> robotNameList;

    // List<TaskStatusCodeInfo> taskStatusList;

    public TaskQueueFileterResponse() {
        super();
    }

    public List<RobotName> getRobotNameList() {
        return robotNameList;
    }

    public void setRobotNameList(List<RobotName> robotNameList) {
        this.robotNameList = robotNameList;
    }

    // public List<TaskStatusCodeInfo> getTaskStatusList() {
    // return taskStatusList;
    // }
    //
    // public void setTaskStatusList(List<TaskStatusCodeInfo> taskStatusList) {
    // this.taskStatusList = taskStatusList;
    // }
}
