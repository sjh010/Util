package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProcessStatusRequest {

    @NotNull
    private Integer taskQueueSequence;

    @NotNull
    private String taskLogStatusCode;

    @Size(max = 500)
    private String remarksContent;

    private MultipartFile[] logFiles;

    @Size(max = 500)
    private String logFileRemarksContent;

    public MultipartFile[] getLogFiles() {
        return logFiles;
    }

    public void setLogFiles(MultipartFile[] logFiles) {
        this.logFiles = logFiles;
    }

    public String getLogFileRemarksContent() {
        return logFileRemarksContent;
    }

    public void setLogFileRemarksContent(String logFileRemarksContent) {
        this.logFileRemarksContent = logFileRemarksContent;
    }

    public String getRemarksContent() {
        return remarksContent;
    }

    public void setRemarksContent(String remarksContent) {
        this.remarksContent = remarksContent;
    }

    public Integer getTaskQueueSequence() {
        return taskQueueSequence;
    }

    public void setTaskQueueSequence(Integer taskQueueSequence) {
        this.taskQueueSequence = taskQueueSequence;
    }

    public String getTaskLogStatusCode() {
        return taskLogStatusCode;
    }

    public void setTaskLogStatusCode(String taskLogStatusCode) {
        this.taskLogStatusCode = taskLogStatusCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProcessStatusRequest [");
        if (taskQueueSequence != null) {
            builder.append("taskQueueSequence=").append(taskQueueSequence).append(", ");
        }
        if (taskLogStatusCode != null) {
            builder.append("taskLogStatusCode=").append(taskLogStatusCode);
        }
        builder.append("]");
        return builder.toString();
    }

}
