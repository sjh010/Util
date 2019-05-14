package com.mobileleader.rpa.view.exception;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;

public class RpaViewAccessDeniedException extends AccessDeniedException {

    private static final long serialVersionUID = 1L;

    private int accessVoteCode = AccessDecisionVoter.ACCESS_DENIED;

    public RpaViewAccessDeniedException(String msg) {
        super(msg);
    }

    public RpaViewAccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }

    public RpaViewAccessDeniedException(String msg, int accessVoteCode) {
        super(msg);
        this.accessVoteCode = accessVoteCode;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getAccessVoteCode() {
        return accessVoteCode;
    }
}
