package com.mobileleader.rpa.api.model.response;

import java.util.List;

public class ProcessListResponse extends RpaApiBaseResponse {

    private List<ProcessInfo> processInfoList;

    public ProcessListResponse(List<ProcessInfo> processInfoList) {
        this.processInfoList = processInfoList;
    }

    public List<ProcessInfo> getProcessInfoList() {
        return processInfoList;
    }
}
