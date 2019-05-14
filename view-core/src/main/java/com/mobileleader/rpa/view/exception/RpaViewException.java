package com.mobileleader.rpa.view.exception;

public class RpaViewException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private RpaViewError rpaViewError;

    public RpaViewException() {}

    public RpaViewException(String message) {
        super(message);
    }

    public RpaViewException(RpaViewError rpaViewError) {
        super(rpaViewError.getErrorMessage());
        this.rpaViewError = rpaViewError;
    }

    public RpaViewException(RpaViewError rpaViewError, Throwable t) {
        super(t);
        this.rpaViewError = rpaViewError;
    }

    public RpaViewException(RpaViewError rpaViewError, String message) {
        super(message);
        this.rpaViewError = rpaViewError;
    }

    public RpaViewError getRpaViewError() {
        return rpaViewError;
    }
}
