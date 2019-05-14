package com.mobileleader.rpa.view.config.security.handler;

import com.mobileleader.rpa.view.config.security.voter.RpaViewAffirmativeBased.RpaViewAccessDecisionVoter;
import com.mobileleader.rpa.view.exception.RpaViewAccessDeniedException;
import com.mobileleader.rpa.view.exception.RpaViewError;
import com.mobileleader.rpa.view.util.RequestHeaderUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

public class RpaViewAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private RedirectStrategy redirectStrategy;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        int accessVoteCode = -1;
        if (accessDeniedException instanceof RpaViewAccessDeniedException) {
            accessVoteCode = ((RpaViewAccessDeniedException) accessDeniedException).getAccessVoteCode();
        }
        SecurityContextHolder.clearContext();
        if (accessVoteCode == RpaViewAccessDecisionVoter.ACCESS_ABSTAIN.getCode()
                && !RequestHeaderUtils.isAjaxRequest(request)) {
            redirectStrategy.sendRedirect(request, response, "/login?error=true&errorCode=730");
        } else {
            request.getSession().invalidate();
            response.sendError(RpaViewError.UNAUTHORIZED.getHttpStatus().value());
        }
    }
}
