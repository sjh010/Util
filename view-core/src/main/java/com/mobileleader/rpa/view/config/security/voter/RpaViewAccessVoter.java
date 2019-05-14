package com.mobileleader.rpa.view.config.security.voter;

import com.mobileleader.rpa.auth.service.authentication.WebAuthenticationService;
import com.mobileleader.rpa.auth.service.authentication.details.RpaUserDetails;
import com.mobileleader.rpa.view.config.security.voter.RpaViewAffirmativeBased.RpaViewAccessDecisionVoter;
import com.mobileleader.rpa.view.util.RequestHeaderUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;

public class RpaViewAccessVoter extends WebExpressionVoter {

    @Autowired
    private WebAuthenticationService webAuthenticationService;

    private static final List<String> PERMIT_ALL_URLS = Arrays.asList("", "/", "/login", "/login_process", "/logout");

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        int result = RpaViewAccessDecisionVoter.ACCESS_DENIED.getCode();
        if (isPermitAllUrl(fi)) {
            result = RpaViewAccessDecisionVoter.ACCESS_GRANTED.getCode();
        } else {
            result = checkAuthentication(authentication, fi);
        }
        return result;
    }

    private int checkAuthentication(Authentication authentication, FilterInvocation fi) {
        if (isValidAuthenticationToken(authentication)) {
            return RpaViewAccessDecisionVoter.ACCESS_GRANTED.getCode();
        } else {
            if (RequestHeaderUtils.isAjaxRequest(fi.getHttpRequest())) {
                return RpaViewAccessDecisionVoter.REST_ACCESS_DENIED.getCode();
            } else {
                return RpaViewAccessDecisionVoter.ACCESS_ABSTAIN.getCode();
            }
        }
    }

    private boolean isPermitAllUrl(FilterInvocation fi) {
        String url = replaceUrlParameters(fi.getRequestUrl());
        if (PERMIT_ALL_URLS.contains(url)) {
            return true;
        } else {
            return false;
        }
    }

    private String replaceUrlParameters(String url) {
        if (url != null && url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        return url;
    }

    private boolean isValidAuthenticationToken(Authentication authentication) {
        boolean isValid = false;
        if (authentication instanceof UsernamePasswordAuthenticationToken && authentication.getDetails() != null
                && authentication.getDetails() instanceof RpaUserDetails) {
            RpaUserDetails userDetails = (RpaUserDetails) authentication.getDetails();
            isValid = webAuthenticationService.verify(userDetails.getAuthenticationToken());
        }
        return isValid;
    }
}
