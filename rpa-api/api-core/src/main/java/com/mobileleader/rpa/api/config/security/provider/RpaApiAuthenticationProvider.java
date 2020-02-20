package com.mobileleader.rpa.api.config.security.provider;

import com.mobileleader.rpa.api.config.security.authentication.RpaApiAuthentication;
import com.mobileleader.rpa.api.config.security.authentication.RpaApiUnauthorizedAuthentication;
import com.mobileleader.rpa.auth.service.authentication.token.AuthenticationTokenDetails;
import java.util.List;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class RpaApiAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof RpaApiUnauthorizedAuthentication)) {
            throw new AuthenticationCredentialsNotFoundException("invalid authentication");
        }
        RpaApiUnauthorizedAuthentication unauthorizedAuthentication = (RpaApiUnauthorizedAuthentication) authentication;
        AuthenticationTokenDetails authenticationTokenDetails =
                createAuthenticationTokenDetails(unauthorizedAuthentication.getAuthenticationToken());
        return new RpaApiAuthentication(authenticationTokenDetails,
                getRoles(authenticationTokenDetails.getCommaSeparatedStringRoles()));
    }

    private AuthenticationTokenDetails createAuthenticationTokenDetails(String authenticationToken) {
        AuthenticationTokenDetails authenticationTokenDetails =
                AuthenticationTokenDetails.deserialize(authenticationToken);
        if (authenticationTokenDetails == null) {
            throw new BadCredentialsException("invalid token");
        }
        return authenticationTokenDetails;
    }

    private List<GrantedAuthority> getRoles(String commaSeparatedStringRoles) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(commaSeparatedStringRoles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RpaApiAuthentication.class.isAssignableFrom(authentication)
                || RpaApiUnauthorizedAuthentication.class.isAssignableFrom(authentication);
    }
}
