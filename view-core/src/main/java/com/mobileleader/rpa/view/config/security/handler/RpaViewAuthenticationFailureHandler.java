package com.mobileleader.rpa.view.config.security.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class RpaViewAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public RpaViewAuthenticationFailureHandler() {
        super.setDefaultFailureUrl("/login?error=true&errorCode=720");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        if (exception.getCause() != null && exception.getCause() instanceof IllegalArgumentException) {
            super.setDefaultFailureUrl("/login?error=true&errorCode=721");
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
