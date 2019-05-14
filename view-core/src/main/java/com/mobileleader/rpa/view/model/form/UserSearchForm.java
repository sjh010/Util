package com.mobileleader.rpa.view.model.form;

import com.mobileleader.rpa.view.query.QueryBuilder;

public class UserSearchForm extends BaseSearchForm {

    private String userInfo;

    private String departmentName;

    private Integer authoritySequence;

    private String useYn;

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getAuthoritySequence() {
        return authoritySequence;
    }

    public void setAuthoritySequence(Integer authoritySequence) {
        this.authoritySequence = authoritySequence;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getUserIdQuery() {
        return QueryBuilder.getWhereLikeClause("tb_user.USR_ID", this.userInfo);
    }

    public String getUserNameQuery() {
        return QueryBuilder.getWhereLikeClause("tb_user.USR_NM", this.userInfo);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserSearchForm [");
        if (userInfo != null) {
            builder.append("userInfo=").append(userInfo).append(", ");
        }
        if (departmentName != null) {
            builder.append("departmentName=").append(departmentName).append(", ");
        }
        if (authoritySequence != null) {
            builder.append("authoritySequence=").append(authoritySequence).append(", ");
        }
        if (useYn != null) {
            builder.append("useYn=").append(useYn);
        }
        builder.append("]");
        return builder.toString();
    }

}
