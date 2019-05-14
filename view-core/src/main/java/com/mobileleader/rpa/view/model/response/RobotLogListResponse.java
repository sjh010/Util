package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.RobotLogList;
import com.mobileleader.rpa.view.model.form.BaseSearchForm;
import java.util.List;

public class RobotLogListResponse {

    private List<RobotLogList> robotLogList;

    private Integer count;

    private BaseSearchForm form;

    public List<RobotLogList> getRobotLogList() {
        return robotLogList;
    }

    public void setRobotLogList(List<RobotLogList> robotLogList) {
        this.robotLogList = robotLogList;
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
