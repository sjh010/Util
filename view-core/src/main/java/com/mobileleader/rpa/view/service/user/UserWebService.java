package com.mobileleader.rpa.view.service.user;

import com.mobileleader.rpa.data.dto.base.Authority;
import com.mobileleader.rpa.data.dto.base.User;
import com.mobileleader.rpa.view.model.form.UserAddForm;
import com.mobileleader.rpa.view.model.form.UserSearchForm;
import com.mobileleader.rpa.view.model.response.UserListResponse;

import java.util.List;

public interface UserWebService {

    User getUser(Integer userSequence);

    UserListResponse getUserList(UserSearchForm userSearchForm);

    Boolean checkUserId(String userId);

    Boolean addUser(UserAddForm userAddForm);

    List<String> getDepartmentNameList();

    List<Authority> getAuthorityList();
}
