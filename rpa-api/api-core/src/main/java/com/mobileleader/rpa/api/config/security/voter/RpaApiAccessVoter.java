package com.mobileleader.rpa.api.config.security.voter;

import com.mobileleader.rpa.api.config.security.authentication.RpaApiAuthentication;
import com.mobileleader.rpa.auth.service.authentication.WebAuthenticationService;
import com.mobileleader.rpa.auth.service.authentication.token.AuthenticationTokenDetails;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;

public class RpaApiAccessVoter extends WebExpressionVoter {

    @Autowired
    private WebAuthenticationService webAuthenticationService;

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        if (!(authentication instanceof RpaApiAuthentication)) {
            return ACCESS_DENIED;
        }
        return checkPermissions((RpaApiAuthentication) authentication, fi.getRequestUrl());
    }

    private int checkPermissions(RpaApiAuthentication authentication, String requestUrl) {
        AuthenticationTokenDetails tokenDetails = authentication.getWebAuthenticationTokenDetails();
        if (isValidToken(tokenDetails)) {
            return ACCESS_GRANTED;
        }
        return ACCESS_DENIED;
    }

    private boolean isValidToken(AuthenticationTokenDetails tokenDetails) {
        return webAuthenticationService.verify(tokenDetails);
    }
}
