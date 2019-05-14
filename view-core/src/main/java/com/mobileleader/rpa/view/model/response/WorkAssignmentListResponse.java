package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.WorkAssignmentList;
import com.mobileleader.rpa.view.model.form.BaseSearchForm;
import java.util.List;

public class WorkAssignmentListResponse {

    private List<WorkAssignmentList> list;

    private Integer totalCount;

    private BaseSearchForm form;

    public WorkAssignmentListResponse() {
        super();
    }

    public List<WorkAssignmentList> getList() {
        return list;
    }

    public void setList(List<WorkAssignmentList> list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BaseSearchForm getForm() {
        return form;
    }

    public void setForm(BaseSearchForm form) {
        this.form = form;
    }


}
