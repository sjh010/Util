package com.mobileleader.rpa.api.model.response;

import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.repository.user.UserInfoSupport;

public class ProcessCheckInResponse extends RpaApiBaseResponse {

    private Integer processSequence;

    private String version;

    private String configManagementUserId;

    private String configManagementUserName;

    /**
     * Create ErrorResponse.
     *
     * @return {@link ProcessCheckInResponse}
     */
    public static ProcessCheckInResponse newErrorInstance(RpaApiError rpaApiError, String configManagementUserId) {
        ProcessCheckInResponse errorResponse = new ProcessCheckInResponse();
        errorResponse.setError(true);
        errorResponse.setErrorCode(rpaApiError.getErrorCode());
        errorResponse.setErrorMessage(rpaApiError.getErrorMessage());
        errorResponse.configManagementUserId = configManagementUserId;
        errorResponse.configManagementUserName = UserInfoSupport.getUserName(configManagementUserId);
        return errorResponse;
    }

    /**
     * Create ErrorResponse.
     *
     * @return {@link ProcessCheckInResponse}
     */
    public static ProcessCheckInResponse newErrorInstance(RpaApiError rpaApiError) {
        ProcessCheckInResponse errorResponse = new ProcessCheckInResponse();
        errorResponse.setError(true);
        errorResponse.setErrorCode(rpaApiError.getErrorCode());
        errorResponse.setErrorMessage(rpaApiError.getErrorMessage());
        return errorResponse;
    }

    public ProcessCheckInResponse() {}

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public ProcessCheckInResponse(Builder builder) {
        processSequence = builder.processSequence;
        version = builder.version;
    }

    public Integer getProcessSequence() {
        return processSequence;
    }

    public String getVersion() {
        return version;
    }

    public String getConfigManagementUserId() {
        return configManagementUserId;
    }

    public String getConfigManagementUserName() {
        return configManagementUserName;
    }

    public static class Builder {
        private Integer processSequence;

        private String version;


        public Builder processSequence(Integer processSequence) {
            this.processSequence = processSequence;
            return this;
        }

        public Builder version(Integer majorVersion, Integer minorVersion) {
            this.version = majorVersion.toString() + "." + minorVersion.toString();
            return this;
        }

        public ProcessCheckInResponse build() {
            return new ProcessCheckInResponse(this);
        }
    }
}
