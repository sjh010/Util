package com.mobileleader.rpa.view.config.security.handler;

import com.mobileleader.rpa.auth.service.authentication.details.RpaUserDetails;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class RpaViewAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private RedirectStrategy redirectStrategy;

    @Value("#{viewProperties['web.session.timeout']}")
    private int sessionTimeout;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            completeAuthentication(request, response, authentication);
        }
        RpaRsaCipher.removeRsaPrivateKeyAttributes(request);
    }


    private void completeAuthentication(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setMaxInactiveInterval(sessionTimeout);
        String redirectUrl = getDefaultUrl(authentication);
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

    private String getDefaultUrl(Authentication authentication) {
        RpaUserDetails userDetails = (RpaUserDetails) authentication.getDetails();
        return userDetails.getDefaultRedirectUrlAfterLogin();
    }
}
