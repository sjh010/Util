package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import com.mobileleader.rpa.view.model.form.BaseSearchForm;
import java.util.List;

public class ProcessHistoryListResponse {

    private List<ProcessVersion> processHistoryList;

    private Integer count;

    private BaseSearchForm form;

    public ProcessHistoryListResponse() {
        super();
    }

    public List<ProcessVersion> getProcessHistoryList() {
        return processHistoryList;
    }

    public void setProcessHistoryList(List<ProcessVersion> processHistoryList) {
        this.processHistoryList = processHistoryList;
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
