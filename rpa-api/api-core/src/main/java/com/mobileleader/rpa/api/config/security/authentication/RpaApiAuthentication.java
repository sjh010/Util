package com.mobileleader.rpa.api.config.security.authentication;

import com.mobileleader.rpa.auth.service.authentication.token.AuthenticationTokenDetails;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class RpaApiAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;

    private AuthenticationTokenDetails authenticationTokenDetails;

    /**
     * Constructor.
     *
     * @param authenticationTokenDetails {@link AuthenticationTokenDetails}
     */
    public RpaApiAuthentication(AuthenticationTokenDetails authenticationTokenDetails) {
        super(AuthorityUtils
                .commaSeparatedStringToAuthorityList(authenticationTokenDetails.getCommaSeparatedStringRoles()));
        setAuthenticated(true);
        this.authenticationTokenDetails = authenticationTokenDetails;
    }

    /**
     * Constructor.
     *
     * @param authenticationTokenDetails {@link AuthenticationTokenDetails}
     * @param authorities roles
     */
    public RpaApiAuthentication(AuthenticationTokenDetails authenticationTokenDetails,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(true);
        this.authenticationTokenDetails = authenticationTokenDetails;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    public AuthenticationTokenDetails getWebAuthenticationTokenDetails() {
        return authenticationTokenDetails;
    }
}
