package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.data.dto.base.Authority;
import com.mobileleader.rpa.view.model.form.BaseSearchForm;
import java.util.List;

public class AuthorityListResponse {

    private List<Authority> authorityList;

    private Integer count;

    private BaseSearchForm form;

    public AuthorityListResponse() {
        super();
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
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
