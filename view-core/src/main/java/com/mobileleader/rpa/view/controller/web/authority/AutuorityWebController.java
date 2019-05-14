package com.mobileleader.rpa.view.controller.web.authority;

import com.mobileleader.rpa.auth.type.RpaAuthority;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authority")
public class AutuorityWebController {

    /**
     * 권한 관리 페이지 로드.
     *
     * @return
     */
    @GetMapping("")
    @Secured(RpaAuthority.SecuredRole.MANAGER_AUTHORITY_READ)
    public String getAuthorityListPage() {
        return "/core/authority/authorityList";
    }

}
