package com.mobileleader.rpa.api.service.process;

import com.mobileleader.rpa.api.model.request.ProcessStatusRequest;
import com.mobileleader.rpa.api.model.response.ProcessDownloadResponse;
import com.mobileleader.rpa.api.model.response.ProcessTaskResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProcessApiService {

    public ProcessTaskResponse getProcessTask(Integer robotSequence);

    public void addProcessTaskLog(ProcessStatusRequest processStatusRequest);

    public ProcessDownloadResponse downloadProcessFile(Integer processVersionSequence, Integer fileSequence,
            HttpServletRequest request, HttpServletResponse response);
}
