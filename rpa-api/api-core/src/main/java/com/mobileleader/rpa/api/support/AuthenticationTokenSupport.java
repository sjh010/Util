package com.mobileleader.rpa.api.support;

import com.mobileleader.rpa.api.config.security.authentication.RpaApiAuthentication;
import com.mobileleader.rpa.auth.service.authentication.token.AuthenticationTokenDetails;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationTokenSupport {

    /**
     * authenticationSequence를 조회한다. 로봇일 경우 robotSequence. 스튜디오/사용자 일 경우 userSequence.
     *
     * @param authenticationType {@link AuthenticationType}
     * @return authenticationSequence
     */
    public static Integer getAuthenticationSequence(AuthenticationType authenticationType) {
        return (getTokenDetails() != null && getTokenDetails().getAuthenticationType().equals(authenticationType))
                ? getTokenDetails().getAuthenticationSequence()
                : null;
    }

    /**
     * 사용자 ID 조회. 로봇 토큰일 경우 로봇 ID
     *
     * @return userId or robotId
     */
    public static String getUserId() {
        return getTokenDetails() != null ? getTokenDetails().getUserId() : null;
    }

    /**
     * 사용자 명 조회. 로봇 토큰일 경우 로봇 명.
     *
     * @return userName or robotName
     */
    public static String getUserName() {
        return getTokenDetails() != null ? getTokenDetails().getUserName() : null;
    }

    /**
     * 인증 타입 조회.
     *
     * @return {@link AuthenticationType}
     */
    public static AuthenticationType getAuthenticationType() {
        return getTokenDetails() != null ? getTokenDetails().getAuthenticationType() : null;
    }

    /**
     * 로봇 토큰 여부 확인.
     *
     * @return boolean
     */
    public static boolean isRobot() {
        return getTokenDetails() != null ? AuthenticationType.ROBOT.equals(getTokenDetails().getAuthenticationType())
                : false;
    }

    /**
     * 스튜디오 토큰 여부 확인.
     *
     * @return boolean
     */
    public static boolean isStudio() {
        return getTokenDetails() != null
                ? AuthenticationType.STUDIO_SIGNED_IN.equals(getTokenDetails().getAuthenticationType())
                : false;
    }

    /**
     * 토큰 정보 로그를 조회한다.
     *
     * @return tokenInfoLog
     */
    public static String getTokenInfoLog() {
        return new StringBuilder(System.getProperty("line.separator")).append("[TokenInfo] Type : ")
                .append(getAuthenticationType()).append(", userId : ").append(getUserId()).append(", userName : ")
                .append(getUserName()).toString();
    }

    private static AuthenticationTokenDetails getTokenDetails() {
        return getAuthentication() != null ? getAuthentication().getWebAuthenticationTokenDetails() : null;
    }

    private static RpaApiAuthentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                ? (RpaApiAuthentication) SecurityContextHolder.getContext().getAuthentication()
                : null;
    }
}
