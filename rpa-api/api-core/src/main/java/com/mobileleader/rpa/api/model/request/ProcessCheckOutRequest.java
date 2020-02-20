package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;

public class ProcessCheckOutRequest {

    @NotNull
    private Integer processSequence;

    public Integer getProcessSequence() {
        return processSequence;
    }

    public void setProcessSequence(Integer processSequence) {
        this.processSequence = processSequence;
    }
}
