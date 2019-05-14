package com.mobileleader.rpa.view.config.security.handler;

import com.mobileleader.rpa.auth.service.authentication.WebAuthenticationService;
import com.mobileleader.rpa.auth.service.authentication.details.RpaUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class RpaViewSessionDestroyHandler implements ApplicationListener<SessionDestroyedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RpaViewSessionDestroyHandler.class);

    @Autowired
    private WebAuthenticationService webAuthenticationService;

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        for (SecurityContext securityContext : event.getSecurityContexts()) {
            deleteToken(securityContext.getAuthentication());
        }
    }

    private void deleteToken(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            RpaUserDetails userDetails = (RpaUserDetails) authentication.getDetails();
            if (userDetails != null) {
                logger.info("[Session Destroy] userId : {}", userDetails.getUserId());
                webAuthenticationService.delete(userDetails.getAuthenticationToken());
            }
        }
    }
}
