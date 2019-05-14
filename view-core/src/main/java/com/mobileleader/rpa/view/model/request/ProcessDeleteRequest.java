package com.mobileleader.rpa.view.model.request;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ProcessDeleteRequest {

    @NotNull
    private List<Integer> processVersionSequenceList;

    @NotNull
    private List<Integer> processSequenceList;

    public List<Integer> getProcessVersionSequenceList() {
        return processVersionSequenceList;
    }

    public void setProcessVersionSequenceList(List<Integer> processVersionSequenceList) {
        this.processVersionSequenceList = processVersionSequenceList;
    }

    public List<Integer> getProcessSequenceList() {
        return processSequenceList;
    }

    public void setProcessSequenceList(List<Integer> processSequenceList) {
        this.processSequenceList = processSequenceList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProcessDeleteRequest [");
        if (processVersionSequenceList != null) {
            builder.append("processVersionSequenceList=").append(processVersionSequenceList).append(", ");
        }
        if (processSequenceList != null) {
            builder.append("processSequenceList=").append(processSequenceList);
        }
        builder.append("]");
        return builder.toString();
    }
}
