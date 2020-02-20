package com.mobileleader.rpa.api.exception;

public class RpaApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private RpaApiError rpaApiError;

    public RpaApiException() {}

    public RpaApiException(String message) {
        super(message);
    }

    public RpaApiException(RpaApiError rpaApiError) {
        super(rpaApiError.getErrorMessage());
        this.rpaApiError = rpaApiError;
    }

    public RpaApiException(RpaApiError rpaApiError, Throwable t) {
        super(t);
        this.rpaApiError = rpaApiError;
    }

    public RpaApiException(RpaApiError rpaApiError, String message) {
        super(message);
        this.rpaApiError = rpaApiError;
    }

    public RpaApiError getRpaApiError() {
        return rpaApiError;
    }
}
