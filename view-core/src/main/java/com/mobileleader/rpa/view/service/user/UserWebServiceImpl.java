package com.mobileleader.rpa.view.service.user;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.base.Authority;
import com.mobileleader.rpa.data.dto.base.User;
import com.mobileleader.rpa.view.data.mapper.base.UserMapper;
import com.mobileleader.rpa.view.model.form.UserAddForm;
import com.mobileleader.rpa.view.model.form.UserSearchForm;
import com.mobileleader.rpa.view.model.response.UserListResponse;
import com.mobileleader.rpa.view.support.UserDetailsSupport;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
public class UserWebServiceImpl implements UserWebService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_USER_READ)
    @Transactional
    public User getUser(Integer userSequence) {
        return userMapper.selectByPrimaryKey(userSequence);
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_USER_READ)
    @Transactional
    public UserListResponse getUserList(UserSearchForm userSearchForm) {

        UserListResponse response = new UserListResponse();
        int count = userMapper.selectCount(userSearchForm);
        response.setCount(count);
        userSearchForm.resetPageNoIfNotValid(count);

        response.setUserList(userMapper.selectUserList(userSearchForm));
        response.setForm(userSearchForm);
        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_USER_READ)
    @Transactional
    public Boolean checkUserId(String userId) {
        if (ObjectUtils.isEmpty(userMapper.selectByUserId(userId))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_USER_MODIFY)
    @Transactional
    public Boolean addUser(UserAddForm userAddForm) {
        User user = new User();

        user.setUserId(userAddForm.getUserId());
        user.setUserName(userAddForm.getUserName());
        user.setDepartmentName(userAddForm.getDepartmentName());
        user.setAuthoritySequence(userAddForm.getAuthoritySequence());
        user.setUseYn(userAddForm.getUseYn());

        user.setUserPassword(passwordEncoder.encode("A1234567!"));
        user.setRegisterId(UserDetailsSupport.getUserId());

        if (userMapper.insertUser(user) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_USER_READ)
    public List<String> getDepartmentNameList() {
        return userMapper.selectDepartmentNameList();
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_USER_READ)
    public List<Authority> getAuthorityList() {
        return userMapper.selectAuthorityList();
    }

}
