package com.mobileleader.rpa.api.model.response;

import com.mobileleader.rpa.api.exception.RpaApiError;

public class ProcessDownloadResponse extends RpaApiBaseResponse {

    /**
     * Create Error Instance.
     *
     * @param rpaApiError {@link RpaApiError}
     * @return
     */
    public static ProcessDownloadResponse newErrorInstance(RpaApiError rpaApiError) {
        ProcessDownloadResponse errorResponse = new ProcessDownloadResponse();
        errorResponse.setError(true);
        errorResponse.setErrorCode(rpaApiError.getErrorCode());
        errorResponse.setErrorMessage(rpaApiError.getErrorMessage());
        return errorResponse;
    }
}
