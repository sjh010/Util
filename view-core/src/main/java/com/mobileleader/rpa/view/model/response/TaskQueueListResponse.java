package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.TaskQueueByWork;
import com.mobileleader.rpa.view.model.form.BaseSearchForm;
import java.util.List;

public class TaskQueueListResponse {

    private List<TaskQueueByWork> taskQueueList;

    private Integer count;

    private BaseSearchForm form;

    public TaskQueueListResponse() {
        super();
    }

    public List<TaskQueueByWork> getTaskQueueList() {
        return taskQueueList;
    }

    public void setTaskQueueList(List<TaskQueueByWork> taskQueueList) {
        this.taskQueueList = taskQueueList;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BaseSearchForm getForm() {
        return form;
    }

    public void setForm(BaseSearchForm form) {
        this.form = form;
    }
}
