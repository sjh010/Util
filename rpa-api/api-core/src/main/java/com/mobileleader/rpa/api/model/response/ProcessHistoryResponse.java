package com.mobileleader.rpa.api.model.response;

import java.util.List;

public class ProcessHistoryResponse extends RpaApiBaseResponse {

    private List<ProcessHistoryInfo> processHistoryInfoList;

    public ProcessHistoryResponse(List<ProcessHistoryInfo> processHistoryInfos) {
        this.processHistoryInfoList = processHistoryInfos;
    }

    public List<ProcessHistoryInfo> getProcessHistoryInfoList() {
        return processHistoryInfoList;
    }
}
