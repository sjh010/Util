package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class LogUploadRequest {

    @NotNull
    private String logTypeCode;

    @NotNull
    private String logStatusCode;

    @Size(max = 500)
    private String remarksContent;

    private MultipartFile[] logFiles;

    @Size(max = 500)
    private String logFileRemarksContent;

    public String getLogFileRemarksContent() {
        return logFileRemarksContent;
    }

    public void setLogFileRemarksContent(String logFileRemarksContent) {
        this.logFileRemarksContent = logFileRemarksContent;
    }

    public String getLogTypeCode() {
        return logTypeCode;
    }

    public void setLogTypeCode(String logTypeCode) {
        this.logTypeCode = logTypeCode;
    }

    public String getLogStatusCode() {
        return logStatusCode;
    }

    public void setLogStatusCode(String logStatusCode) {
        this.logStatusCode = logStatusCode;
    }

    public String getRemarksContent() {
        return remarksContent;
    }

    public void setRemarksContent(String remarksContent) {
        this.remarksContent = remarksContent;
    }

    public MultipartFile[] getLogFiles() {
        return logFiles;
    }

    public void setLogFiles(MultipartFile[] logFiles) {
        this.logFiles = logFiles;
    }
}
