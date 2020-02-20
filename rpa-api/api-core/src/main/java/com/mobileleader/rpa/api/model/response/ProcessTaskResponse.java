package com.mobileleader.rpa.api.model.response;

import java.util.List;

public class ProcessTaskResponse extends RpaApiBaseResponse {

    private boolean exist = false;

    private Integer taskQueueSequence;

    private Integer processVersionSequence;

    private String processVersion;

    private List<Integer> fileSequenceList;


    public static ProcessTaskResponse newInstance(Builder builder) {
        return new ProcessTaskResponse(builder);
    }

    public static ProcessTaskResponse newEmptyInstance() {
        return new ProcessTaskResponse();
    }

    public ProcessTaskResponse() {
        exist = false;
    }

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public ProcessTaskResponse(Builder builder) {
        exist = true;
        taskQueueSequence = builder.taskQueueSequence;
        processVersionSequence = builder.processVersionSequence;
        processVersion = builder.processVersion;
        fileSequenceList = builder.fileSequenceList;
    }

    public static class Builder {
        private Integer taskQueueSequence;

        private Integer processVersionSequence;

        private String processVersion;

        private List<Integer> fileSequenceList;

        public Builder taskQueueSequence(Integer taskQueueSequence) {
            this.taskQueueSequence = taskQueueSequence;
            return this;
        }

        public Builder processVersionSequence(Integer processVersionSequence) {
            this.processVersionSequence = processVersionSequence;
            return this;
        }

        public Builder processVersion(Integer majorVersion, Integer minorVersion) {
            this.processVersion = majorVersion.toString() + "." + minorVersion;
            return this;
        }

        public Builder fileSequenceList(List<Integer> fileSequenceList) {
            this.fileSequenceList = fileSequenceList;
            return this;
        }
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public Integer getTaskQueueSequence() {
        return taskQueueSequence;
    }

    public void setTaskQueueSequence(Integer taskQueueSequence) {
        this.taskQueueSequence = taskQueueSequence;
    }

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public void setProcessVersionSequence(Integer processVersionSequence) {
        this.processVersionSequence = processVersionSequence;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    public List<Integer> getFileSequenceList() {
        return fileSequenceList;
    }

    public void setFileSequenceList(List<Integer> fileSequenceList) {
        this.fileSequenceList = fileSequenceList;
    }

}
