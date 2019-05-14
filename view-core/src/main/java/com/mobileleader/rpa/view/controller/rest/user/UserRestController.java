package com.mobileleader.rpa.view.controller.rest.user;

import com.mobileleader.rpa.data.dto.base.User;
import com.mobileleader.rpa.view.model.form.UserAddForm;
import com.mobileleader.rpa.view.model.form.UserSearchForm;
import com.mobileleader.rpa.view.model.response.UserFilterResponse;
import com.mobileleader.rpa.view.model.response.UserListResponse;
import com.mobileleader.rpa.view.service.user.UserWebService;
import com.mobileleader.rpa.view.support.RepositoryReloadSupport;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserWebService userService;

    /**
     * 사용자 조회.
     *
     * @param userSequence 사용자 순번
     * @return {@link User}
     */
    @PostMapping(value = "")
    public ResponseEntity<User> getUser(@RequestParam(required = true, name = "userSequence") Integer userSequence) {
        return new ResponseEntity<User>(userService.getUser(userSequence), HttpStatus.OK);
    }

    /**
     * 사용자 리스트 조회.
     *
     * @param userSearchForm 검색폼
     * @return 사용자 리스트
     */
    @PostMapping(value = "/list")
    public ResponseEntity<UserListResponse> getUserList(@RequestBody @Valid UserSearchForm userSearchForm,
            BindingResult bindingResult) {

        logger.info("** Get User List ** sortKey : {}, sortOrder : {}", userSearchForm.getSortKey(),
                userSearchForm.getSortOrder());

        return new ResponseEntity<UserListResponse>(userService.getUserList(userSearchForm), HttpStatus.OK);
    }

    /**
     * 사용자 목록 필터 조회.
     *
     * @return {@link UserFilterResponse}
     */
    @PostMapping(value = "/list/filter")
    public ResponseEntity<UserFilterResponse> getUserFilterList() {
        UserFilterResponse response = new UserFilterResponse();
        response.setDepartmentNameList(userService.getDepartmentNameList());
        response.setAuthorityList(userService.getAuthorityList());

        return new ResponseEntity<UserFilterResponse>(response, HttpStatus.OK);
    }

    /**
     * 아이디 중복 체크.
     *
     * @param userId 체크할 ID
     * @return 중복여부
     */
    @PostMapping(value = "/checkId")
    public ResponseEntity<Boolean> checkUserId(@RequestParam(required = true, name = "userId") String userId) {
        logger.info("** Check User Id ** {}", userId);

        return new ResponseEntity<Boolean>(userService.checkUserId(userId), HttpStatus.OK);
    }

    /**
     * 사용자 추가.
     *
     * @param userAddForm 사용자 정보
     * @return 추가여부
     */
    @PostMapping(value = "/add")
    public ResponseEntity<Boolean> addUser(@RequestBody @Valid UserAddForm userAddForm, BindingResult bindingResult) {
        logger.info("** Add User ** {}", userAddForm.toString());
        boolean isSuccess = userService.addUser(userAddForm);
        if (isSuccess) {
            RepositoryReloadSupport.reloadUserRepository();
        }
        return new ResponseEntity<Boolean>(isSuccess, HttpStatus.OK);
    }

    /**
     * 부서명 리스트 조회.
     *
     * @return 부서명 리스트
     */
    @PostMapping(value = "/department/list")
    public ResponseEntity<List<String>> getDepartmentList() {
        logger.info("** Get Department List **");

        return new ResponseEntity<List<String>>(userService.getDepartmentNameList(), HttpStatus.OK);
    }
}
