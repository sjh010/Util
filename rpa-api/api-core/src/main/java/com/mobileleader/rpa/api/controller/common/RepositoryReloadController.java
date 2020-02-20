package com.mobileleader.rpa.api.controller.common;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;
import com.mobileleader.rpa.repository.user.UserInfoSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common/repository")
public class RepositoryReloadController {

    /**
     * UserRepository를 Reload한다.
     *
     * @return void
     */
    @PostMapping("/user/reload")
    @Secured(RpaAuthority.SecuredRole.MANAGER_USER_MODIFY)
    public ResponseEntity<Void> reloadUserRepository() {
        UserInfoSupport.reload();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * CodeAndConfig Repository를 Reload한다.
     *
     * @return void
     */
    @PostMapping("/code/reload")
    @PreAuthorize("hasAnyRole('" + RpaAuthority.SecuredRole.MANAGER_COMMON_CODE_MODIFY + "','"
            + RpaAuthority.SecuredRole.MANAGER_COMMON_CONFIG_MODIFY + "')")
    public ResponseEntity<Void> reloadCodeAndConfigRepository() {
        CodeAndConfigSupport.reload();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
