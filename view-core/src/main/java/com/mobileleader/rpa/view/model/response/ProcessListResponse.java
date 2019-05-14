package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.ProcessInfo;
import com.mobileleader.rpa.view.model.form.BaseSearchForm;
import java.util.List;

public class ProcessListResponse {

    private List<ProcessInfo> processList;

    private Integer count;

    private BaseSearchForm form;

    public List<ProcessInfo> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessInfo> processList) {
        this.processList = processList;
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
