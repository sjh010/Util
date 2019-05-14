package com.mobileleader.rpa.view.controller.web.login;

import com.mobileleader.rpa.auth.service.authentication.details.RpaUserDetails;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher;
import com.mobileleader.rpa.view.exception.RpaViewError;
import com.mobileleader.rpa.view.exception.RpaViewException;
import com.mobileleader.rpa.view.util.RequestHeaderUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginWebController {

    @Autowired
    private RedirectStrategy redirectStrategy;

    @Autowired
    private RpaRsaCipher rpaRsaCipher;

    /**
     * Login View 조회.
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param isError 로그인 에러 유무
     * @param errorCode 에러코드
     * @return
     */
    @GetMapping("")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name = "error", required = false) Boolean isError,
            @RequestParam(name = "errorCode", required = false) Integer errorCode) {
        redirectDefaultUrl(request, response);
        ModelAndView mav = new ModelAndView("/core/login/login");
        if (isError != null && isError) {
            mav.addObject("error", true);
            mav.addObject("errorCode", errorCode);
        }
        rpaRsaCipher.initializeRsa(request);
        return mav;
    }

    private void redirectDefaultUrl(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken && authentication.isAuthenticated()) {
            RpaUserDetails userDetails = (RpaUserDetails) authentication.getDetails();
            try {
                if (!RequestHeaderUtils.isAjaxRequest(request)) {
                    redirectStrategy.sendRedirect(request, response, userDetails.getDefaultRedirectUrlAfterLogin());
                }
            } catch (IOException e) {
                throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR, "Redirect IOException");
            }
        }
    }
}
