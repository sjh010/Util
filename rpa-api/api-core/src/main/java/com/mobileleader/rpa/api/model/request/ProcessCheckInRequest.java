package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProcessCheckInRequest {

    @NotNull
    private Integer processSequence;

    @Size(max = 500)
    private String remarksContent;

    @NotNull
    private MultipartFile[] processFile;

    @Size(max = 500)
    private String processFileRemarksContent;

    public String getProcessFileRemarksContent() {
        return processFileRemarksContent;
    }

    public void setProcessFileRemarksContent(String processFileRemarksContent) {
        this.processFileRemarksContent = processFileRemarksContent;
    }

    public Integer getProcessSequence() {
        return processSequence;
    }

    public void setProcessSequence(Integer processSequence) {
        this.processSequence = processSequence;
    }

    public String getRemarksContent() {
        return remarksContent;
    }

    public void setRemarksContent(String remarksContent) {
        this.remarksContent = remarksContent;
    }

    public MultipartFile[] getProcessFile() {
        return processFile;
    }

    public void setProcessFile(MultipartFile[] processFile) {
        this.processFile = processFile;
    }
}
