package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.RobotList;
import com.mobileleader.rpa.view.model.form.BaseSearchForm;
import java.util.List;

public class RobotListResponse {

    private List<RobotList> list;

    private Integer totalCount;

    private BaseSearchForm form;

    public List<RobotList> getList() {
        return list;
    }

    public void setList(List<RobotList> list) {
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
