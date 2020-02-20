package com.mobileleader.rpa.api.service.process;

import com.mobileleader.rpa.api.model.request.ProcessCheckInRequest;
import com.mobileleader.rpa.api.model.request.ProcessCheckOutRequest;
import com.mobileleader.rpa.api.model.request.ProcessRegistRequest;
import com.mobileleader.rpa.api.model.response.ProcessCheckInResponse;
import com.mobileleader.rpa.api.model.response.ProcessCheckOutResponse;
import com.mobileleader.rpa.api.model.response.ProcessHistoryResponse;
import com.mobileleader.rpa.api.model.response.ProcessListResponse;

public interface ProcessCheckInOutService {
    public ProcessCheckInResponse registProcess(ProcessRegistRequest request);

    public ProcessCheckInResponse checkInProcess(ProcessCheckInRequest request);

    public ProcessCheckOutResponse checkOutProcess(ProcessCheckOutRequest request);

    public ProcessListResponse getProcessInfoList();

    public ProcessHistoryResponse getProcessHistory(Integer processVersionSequence);

    public ProcessCheckOutResponse cancelCheckOutProcess(ProcessCheckOutRequest request);
}
