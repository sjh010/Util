package com.mobileleader.rpa.view.controller.web.user;

import com.mobileleader.rpa.auth.type.RpaAuthority;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserWebController {

    /**
     * 사용자 관리 페이지 로드.
     *
     * @return
     */
    @GetMapping()
    @Secured(RpaAuthority.SecuredRole.MANAGER_USER_READ)
    public String getUserManagementPage() {
        return "/core/user/userList";
    }

}
