package com.mobileleader.rpa.view.model.form;

import com.mobileleader.rpa.view.query.QueryBuilder;

public class AuthoritySearchForm extends BaseSearchForm {

    // 권한명
    private String authorityName;

    // 사용여부
    private String useYn;

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getAuthorityNameQuery() {
        return QueryBuilder.getWhereLikeClause("tb_auth.ATH_NM", this.authorityName);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AuthoritySearchForm [");
        if (authorityName != null) {
            builder.append("authorityName=").append(authorityName).append(", ");
        }
        if (useYn != null) {
            builder.append("useYn=").append(useYn);
        }
        builder.append("]");
        return builder.toString();
    }

}
