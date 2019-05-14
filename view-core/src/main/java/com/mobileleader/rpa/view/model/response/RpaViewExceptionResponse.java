package com.mobileleader.rpa.view.model.response;

public class RpaViewExceptionResponse {
    private boolean isError = false;

    private String exceptionMessage;

    private String errorMessage;

    private int errorCode;

    private String dateTime;

    public RpaViewExceptionResponse() {}

    /**
     * RpaApiCoreExceptionResponse build.
     *
     * @param builder {@link Builder}
     */
    public RpaViewExceptionResponse(Builder builder) {
        isError = true;
        exceptionMessage = builder.exceptionMessage;
        errorMessage = builder.errorMessage;
        errorCode = builder.errorCode;
        dateTime = builder.dateTime;
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

        public RpaViewExceptionResponse build() {
            dateTime();
            return new RpaViewExceptionResponse(this);
        }
    }

    public boolean isError() {
        return isError;
    }


    public void setError(boolean isError) {
        this.isError = isError;
    }


    public String getExceptionMessage() {
        return exceptionMessage;
    }


    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public int getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


    public String getDateTime() {
        return dateTime;
    }


    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
