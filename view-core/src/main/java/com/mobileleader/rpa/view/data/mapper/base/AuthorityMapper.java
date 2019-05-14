package com.mobileleader.rpa.view.data.mapper.base;

import com.mobileleader.rpa.data.dto.base.Authority;
import com.mobileleader.rpa.view.model.form.AuthoritySearchForm;

import java.util.List;

public interface AuthorityMapper {

    List<Authority> selectAuthorityList(AuthoritySearchForm authoritySearchForm);

    Integer selectCount(AuthoritySearchForm authoritySearchForm);
}
