package com.mobileleader.rpa.view.support;

import com.mobileleader.rpa.auth.service.authentication.details.RpaUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserDetailsSupport {

    public static String getUserId() {
        return getUserDetails() != null ? getUserDetails().getUserId() : null;
    }

    public static String getUserName() {
        return getUserDetails() != null ? getUserDetails().getUserName() : null;
    }

    public static String getDefaultRedirectUrl() {
        return getUserDetails() != null ? getUserDetails().getDefaultRedirectUrlAfterLogin() : null;
    }

    public static String getAuthenticationToken() {
        return getUserDetails() != null ? getUserDetails().getAuthenticationToken() : null;
    }

    private static RpaUserDetails getUserDetails() {
        return getAuthentication() != null ? (RpaUserDetails) getAuthentication().getDetails() : null;
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getUserInfoLog() {
        return new StringBuilder(System.getProperty("line.separator")).append("[UserInfo] userId : ")
                .append(getUserId()).append(", userName : ").append(getUserName()).toString();
    }
}
