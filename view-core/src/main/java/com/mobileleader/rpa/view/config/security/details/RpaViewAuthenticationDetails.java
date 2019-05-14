package com.mobileleader.rpa.view.config.security.details;

import com.mobileleader.rpa.utils.cipher.RpaRsaCipher;
import java.security.PrivateKey;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class RpaViewAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = 1L;

    private final String userAgent;

    private final PrivateKey privateKey;

    private final String saveEmail;

    /**
     * Constructor.
     *
     * @param request {@link HttpServletRequest}
     */
    public RpaViewAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        this.saveEmail = request.getParameter("saveEmail");
        this.privateKey = RpaRsaCipher.getPrivateKeyFromSession(request);
    }

    public String getSaveEmail() {
        return saveEmail;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
