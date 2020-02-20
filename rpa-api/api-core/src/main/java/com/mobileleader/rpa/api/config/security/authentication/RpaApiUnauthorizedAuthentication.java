package com.mobileleader.rpa.api.config.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class RpaApiUnauthorizedAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;

    private String authenticationToken;

    /**
     * Constructor.
     *
     * @param authenticationToken token
     */
    public RpaApiUnauthorizedAuthentication(String authenticationToken) {
        super(null);
        this.authenticationToken = authenticationToken;
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }


    public String getAuthenticationToken() {
        return authenticationToken;
    }
}
