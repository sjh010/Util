package com.mobileleader.rpa.api.model.response;

import com.mobileleader.rpa.repository.user.UserInfoSupport;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class ProcessVersionInfo {

    private Integer processVersionSequence;

    private String version;

    private String remarksContent;

    private List<Integer> fileSequenceList = new ArrayList<>();

    private String activationYn;

    private String registDateTime;

    private String registerId;

    private String registerName;

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public void setProcessVersionSequence(Integer processVersionSequence) {
        this.processVersionSequence = processVersionSequence;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemarksContent() {
        return remarksContent;
    }

    public void setRemarksContent(String remarksContent) {
        this.remarksContent = remarksContent;
    }

    public List<Integer> getFileSequenceList() {
        return fileSequenceList;
    }

    /**
     * Set fileSequenceList.
     *
     * @param commaSeparatedFileSequenceList commaSeparatedFileSequenceList
     */
    public void setFileSequenceList(String commaSeparatedFileSequenceList) {
        if (StringUtils.isEmpty(fileSequenceList)) {
            return;
        }
        try {
            for (String fileSequenceValue : StringUtils
                    .commaDelimitedListToStringArray(commaSeparatedFileSequenceList)) {
                this.fileSequenceList.add(Integer.parseInt(fileSequenceValue));
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    public String getActivationYn() {
        return activationYn;
    }

    public void setActivationYn(String activationYn) {
        this.activationYn = activationYn;
    }

    public String getRegistDateTime() {
        return registDateTime;
    }

    public void setRegistDateTime(String registDateTime) {
        this.registDateTime = registDateTime;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
        this.registerName = UserInfoSupport.getUserName(registerId);
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }
}
