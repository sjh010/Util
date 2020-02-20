package com.mobileleader.rpa.api.model.response;

import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.repository.user.UserInfoSupport;

public class ProcessCheckOutResponse extends RpaApiBaseResponse {

    private Integer processSequence;

    private String processName;

    private String configManagementStatusCode;

    private String configManagementStatusCodeName;

    private String configManagementUserId;

    private String configManagementUserName;

    /**
     * Create ErrorResponse.
     *
     * @return {@link ProcessCheckOutResponse}
     */
    public static ProcessCheckOutResponse newErrorInstance(RpaApiError rpaApiError,
            String configurationManagementUserId) {
        ProcessCheckOutResponse errorResponse = new ProcessCheckOutResponse();
        errorResponse.setError(true);
        errorResponse.setErrorCode(rpaApiError.getErrorCode());
        errorResponse.setErrorMessage(rpaApiError.getErrorMessage());
        errorResponse.configManagementUserId = configurationManagementUserId;
        errorResponse.configManagementUserName = UserInfoSupport.getUserName(configurationManagementUserId);
        return errorResponse;
    }

    /**
     * Create ErrorResponse.
     *
     * @return {@link ProcessCheckOutResponse}
     */
    public static ProcessCheckOutResponse newErrorInstance(RpaApiError rpaApiError) {
        ProcessCheckOutResponse errorResponse = new ProcessCheckOutResponse();
        errorResponse.setError(true);
        errorResponse.setErrorCode(rpaApiError.getErrorCode());
        errorResponse.setErrorMessage(rpaApiError.getErrorMessage());
        return errorResponse;
    }

    public ProcessCheckOutResponse() {}

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public ProcessCheckOutResponse(Builder builder) {
        processSequence = builder.processSequence;
        processName = builder.processName;
        configManagementStatusCode = builder.configManagementStatusCode;
        configManagementStatusCodeName = builder.configManagementStatusCodeName;
    }

    public Integer getProcessSequence() {
        return processSequence;
    }

    public String getProcessName() {
        return processName;
    }

    public String getConfigManagementStatusCode() {
        return configManagementStatusCode;
    }

    public String getConfigManagementStatusCodeName() {
        return configManagementStatusCodeName;
    }

    public String getConfigManagementUserId() {
        return configManagementUserId;
    }

    public String getConfigManagementUserName() {
        return configManagementUserName;
    }

    public static class Builder {
        private Integer processSequence;

        private String processName;

        private String configManagementStatusCode;

        private String configManagementStatusCodeName;

        public Builder processSequence(Integer processSequence) {
            this.processSequence = processSequence;
            return this;
        }

        public Builder processName(String processName) {
            this.processName = processName;
            return this;
        }

        public Builder configManagementStatusCode(String configManagementStatusCode) {
            this.configManagementStatusCode = configManagementStatusCode;
            return this;
        }

        public Builder configManagementStatusCodeName(String configManagementStatusCodeName) {
            this.configManagementStatusCodeName = configManagementStatusCodeName;
            return this;
        }

        public ProcessCheckOutResponse build() {
            return new ProcessCheckOutResponse(this);
        }
    }
}
