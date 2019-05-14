package com.mobileleader.rpa.view.model.request;

import java.util.List;

public class WorkAssignmentDeleteRequest {
    private List<Integer> workSequenceList;

    public WorkAssignmentDeleteRequest() {
        super();
    }

    public List<Integer> getWorkSequenceList() {
        return workSequenceList;
    }

    public void setWorkSequenceList(List<Integer> workSequenceList) {
        this.workSequenceList = workSequenceList;
    }
}
