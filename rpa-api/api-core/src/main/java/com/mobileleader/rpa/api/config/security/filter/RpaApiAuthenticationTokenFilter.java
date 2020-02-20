package com.mobileleader.rpa.api.config.security.filter;

import com.mobileleader.rpa.api.config.security.authentication.RpaApiUnauthorizedAuthentication;
import com.mobileleader.rpa.auth.service.authentication.token.AuthenticationTokenDetails;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class RpaApiAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authenticationToken = getAuthenticationToken(request);
        if (authenticationToken != null) {
            SecurityContextHolder.getContext()
                    .setAuthentication(new RpaApiUnauthorizedAuthentication(authenticationToken));
        } else {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private String getAuthenticationToken(HttpServletRequest request) {
        return request.getHeader(AuthenticationTokenDetails.AUTHENTICATION_TOKEN_HEADER);
    }
}
