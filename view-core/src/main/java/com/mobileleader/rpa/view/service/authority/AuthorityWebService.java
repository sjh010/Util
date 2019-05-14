package com.mobileleader.rpa.view.service.authority;

import com.mobileleader.rpa.view.model.form.AuthoritySearchForm;
import com.mobileleader.rpa.view.model.response.AuthorityListResponse;

public interface AuthorityWebService {

    AuthorityListResponse getAuthorityList(AuthoritySearchForm authoritySearchForm);
}
