package com.mobileleader.rpa.view.service.process;

import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import com.mobileleader.rpa.data.dto.common.CommonCode;
import com.mobileleader.rpa.view.data.dto.ProcessNameVersion;
import com.mobileleader.rpa.view.model.form.ProcessHistorySearchForm;
import com.mobileleader.rpa.view.model.form.ProcessSearchForm;
import com.mobileleader.rpa.view.model.request.ProcessDeleteRequest;
import com.mobileleader.rpa.view.model.response.ProcessHistoryListResponse;
import com.mobileleader.rpa.view.model.response.ProcessListResponse;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProcessWebService {

    ProcessVersion getProcessVersion(Integer processVersionSequence);

    ProcessListResponse getProcessList(ProcessSearchForm processSearchForm);

    ProcessHistoryListResponse getProcessHistory(ProcessHistorySearchForm processHistorySearchForm);

    Integer checkInProcess(List<Integer> processVersionSequenceList);

    Integer deleteProcess(ProcessDeleteRequest processDeleteRequest);

    void downloadProcess(List<Integer> processVersionSequenceList, HttpServletRequest request,
            HttpServletResponse response);

    Integer setProcessStatusOff(Integer processVersionSequence);

    Integer setProcessStatusOn(Integer processVersionSequence);

    List<CommonCode> getConfigManagementStatusCodeList();

    List<ProcessNameVersion> findActiveProcessVersion(List<Integer> processVersionSequenceList);
}
