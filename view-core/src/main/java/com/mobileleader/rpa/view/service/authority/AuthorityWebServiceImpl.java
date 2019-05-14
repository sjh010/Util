package com.mobileleader.rpa.view.service.authority;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.view.data.mapper.base.AuthorityMapper;
import com.mobileleader.rpa.view.model.form.AuthoritySearchForm;
import com.mobileleader.rpa.view.model.response.AuthorityListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorityWebServiceImpl implements AuthorityWebService {

    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_AUTHORITY_READ)
    @Transactional
    public AuthorityListResponse getAuthorityList(AuthoritySearchForm authoritySearchForm) {

        AuthorityListResponse response = new AuthorityListResponse();
        int count = authorityMapper.selectCount(authoritySearchForm);
        response.setCount(count);
        authoritySearchForm.resetPageNoIfNotValid(count);

        response.setAuthorityList(authorityMapper.selectAuthorityList(authoritySearchForm));

        response.setForm(authoritySearchForm);
        return response;
    }

}
