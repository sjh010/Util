package com.mobileleader.rpa.api.model.response;

public class RpaApiExceptionResponse {

    private boolean error = true;

    private String exceptionMessage;

    private String errorMessage;

    private int errorCode;

    private String dateTime;

    public RpaApiExceptionResponse() {}

    /**
     * RpaApiCoreExceptionResponse build.
     *
     * @param builder {@link Builder}
     */
    public RpaApiExceptionResponse(Builder builder) {
        error = true;
        exceptionMessage = builder.exceptionMessage;
        errorMessage = builder.errorMessage;
        errorCode = builder.errorCode;
        dateTime = builder.dateTime;
    }

    public boolean isError() {
        return error;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public static class Builder {
        private String exceptionMessage;

        private String errorMessage;

        private int errorCode;

        private String dateTime;

        public Builder exceptionMessage(String exceptionMessage) {
            this.exceptionMessage = exceptionMessage;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder errorCode(int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        private Builder dateTime() {
            // this.dateTime = DateTimeUtils.getNowString();
            return this;
        }

        public RpaApiExceptionResponse build() {
            dateTime();
            return new RpaApiExceptionResponse(this);
        }
    }
}
