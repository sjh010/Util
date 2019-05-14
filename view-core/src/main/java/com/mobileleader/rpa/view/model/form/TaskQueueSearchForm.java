package com.mobileleader.rpa.view.model.form;

public class TaskQueueSearchForm extends BaseSearchForm {

    private Integer workSequence;

    private String robotName = "";

    private String taskStatusCode = "";



    public TaskQueueSearchForm() {
        super();
    }

    public Integer getWorkSequence() {
        return workSequence;
    }

    public void setWorkSequence(Integer workSequence) {
        this.workSequence = workSequence;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getTaskStatusCode() {
        return taskStatusCode;
    }

    public void setTaskStatusCode(String taskStatusCode) {
        this.taskStatusCode = taskStatusCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskQueueSearchForm [workSequence=");
        builder.append(workSequence);
        builder.append(", robotName=");
        builder.append(robotName);
        builder.append(", taskStatusCode=");
        builder.append(taskStatusCode);
        builder.append("]");
        return builder.toString();
    }
}
