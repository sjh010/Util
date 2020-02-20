package com.mobileleader.rpa.api.service.log;

import com.mobileleader.rpa.api.model.request.LogUploadRequest;

public interface LogUploadService {

    public void uploadStudioLog(LogUploadRequest request);

    public void uploadRobotLog(LogUploadRequest request);
}
