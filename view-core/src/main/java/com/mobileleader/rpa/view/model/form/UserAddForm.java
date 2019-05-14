package com.mobileleader.rpa.view.model.form;

import javax.validation.constraints.NotNull;

public class UserAddForm {

    @NotNull
    private String userId;

    @NotNull
    private String userName;

    @NotNull
    private String departmentName;

    @NotNull
    private Short authoritySequence;

    @NotNull
    private String useYn;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Short getAuthoritySequence() {
        return authoritySequence;
    }

    public void setAuthoritySequence(Short authoritySequence) {
        this.authoritySequence = authoritySequence;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

}
