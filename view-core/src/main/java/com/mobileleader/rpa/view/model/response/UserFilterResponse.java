package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.data.dto.base.Authority;

import java.util.List;

public class UserFilterResponse {

    List<String> departmentNameList;

    List<Authority> authorityList;

    public List<String> getDepartmentNameList() {
        return departmentNameList;
    }

    public void setDepartmentNameList(List<String> departmentNameList) {
        this.departmentNameList = departmentNameList;
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }

}
