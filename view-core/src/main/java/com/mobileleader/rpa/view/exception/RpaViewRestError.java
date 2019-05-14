package com.mobileleader.rpa.view.exception;

public enum RpaViewRestError {
    // @formatter:off
    INTERNAL_SERVER_ERROR(700, "Internal server error"),
    ILLEGAL_ARGUMENT(710, "IllegalArgument"),
    INVALID_PARAMETER(711, "Invalid Parameter"),

    AUTHENTICATION_ERROR(720, "Authentication failed"),
    UNKNOWN_USER(721, "Unknown user"),
    INVALID_PASSWORD(722, "Invalid password"),
    PERMISSION_DENIED(723, "No permission"),
    INVALID_AUTHENTICATION(724, "Invalid authentication"),
    AUTHORITY_NOT_FOUND(725, "User Authority not found"),

    SQL_ERROR(730, "SQL Error"),

    FILE_IO_ERROR(740, "File IO Error"),

    SCHEDULE_ERROR(750, "Schedule Error"),

    ENCODE_ERROR(760, "UnsupportedEncoding Error");
    // @formatter:on

    private final int errorCode;

    private final String errorMessage;

    private RpaViewRestError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
