package com.mobileleader.rpa.view.exception;

import org.springframework.http.HttpStatus;

public enum RpaViewError {
    // @formatter:off
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "File Not Found");
    // @formatter:on

    private final HttpStatus httpStatus;

    private final String errorMessage;

    private RpaViewError(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
