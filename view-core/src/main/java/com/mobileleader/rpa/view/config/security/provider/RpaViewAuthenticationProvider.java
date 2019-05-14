package com.mobileleader.rpa.view.config.security.provider;

import com.mobileleader.rpa.auth.model.request.WebAuthenticationRequest;
import com.mobileleader.rpa.auth.service.authentication.WebAuthenticationService;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher.EncryptConvertType;
import com.mobileleader.rpa.view.config.security.details.RpaViewAuthenticationDetails;
import com.mobileleader.rpa.view.exception.RpaViewError;
import com.mobileleader.rpa.view.exception.RpaViewException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class RpaViewAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(RpaViewAuthenticationProvider.class);

    @Autowired
    private WebAuthenticationService webAuthenticationService;

    @Autowired
    private RpaRsaCipher rpaRsaCipher;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RpaViewAuthenticationDetails details = (RpaViewAuthenticationDetails) authentication.getDetails();
        String userId = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        PrivateKey privateKey = details.getPrivateKey();
        if (authentication instanceof UsernamePasswordAuthenticationToken && privateKey != null) {
            try {
                password = rpaRsaCipher.decrypt(privateKey, password, EncryptConvertType.BASE64);
            } catch (GeneralSecurityException e) {
                logger.error("[Password decrypt failed]", e);
                throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR, "decrypt failed");
            }
        }
        try {
            return webAuthenticationService.create(new WebAuthenticationRequest.Builder().userId(userId)
                    .password(password).userAgent(details.getUserAgent()).build());
        } catch (IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException("Invalid ID or Password", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
