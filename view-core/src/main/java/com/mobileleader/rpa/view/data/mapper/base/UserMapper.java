package com.mobileleader.rpa.view.data.mapper.base;

import com.mobileleader.rpa.data.dto.base.Authority;
import com.mobileleader.rpa.data.dto.base.User;
import com.mobileleader.rpa.view.data.dto.UserList;
import com.mobileleader.rpa.view.model.form.UserSearchForm;

import java.util.List;

public interface UserMapper {

    User selectByPrimaryKey(Integer userSequence);

    List<UserList> selectUserList(UserSearchForm userSearchForm);

    Integer selectCount(UserSearchForm userSearchForm);

    Integer selectByUserId(String userId);

    int insertUser(User user);

    List<String> selectDepartmentNameList();

    List<Authority> selectAuthorityList();
}