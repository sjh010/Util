package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.UserList;
import com.mobileleader.rpa.view.model.form.BaseSearchForm;
import java.util.List;

public class UserListResponse {

    private List<UserList> userList;

    private Integer count;

    private BaseSearchForm form;

    public UserListResponse() {
        super();
    }

    public List<UserList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserList> userList) {
        this.userList = userList;
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
