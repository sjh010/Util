package com.mobileleader.rpa.view.exception;

public class RpaViewRestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private RpaViewRestError rpaViewRestError;

    public RpaViewRestException() {}

    public RpaViewRestException(String message) {
        super(message);
    }

    public RpaViewRestException(RpaViewRestError rpaViewRestError) {
        super(rpaViewRestError.getErrorMessage());
        this.rpaViewRestError = rpaViewRestError;
    }

    public RpaViewRestException(RpaViewRestError rpaViewRestError, Throwable t) {
        super(t);
        this.rpaViewRestError = rpaViewRestError;
    }

    public RpaViewRestException(RpaViewRestError rpaViewRestError, String message) {
        super(message);
        this.rpaViewRestError = rpaViewRestError;
    }

    public RpaViewRestError getRpaViewRestError() {
        return rpaViewRestError;
    }
}
