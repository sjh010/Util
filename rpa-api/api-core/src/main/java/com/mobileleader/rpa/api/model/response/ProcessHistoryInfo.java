package com.mobileleader.rpa.api.model.response;

import com.mobileleader.rpa.data.dto.biz.ProcessHistory;
import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;
import com.mobileleader.rpa.repository.user.UserInfoSupport;
import com.mobileleader.rpa.utils.datetime.DateTimeUtils;

public class ProcessHistoryInfo {

    private Integer processHistorySequence;

    private Integer processVersionSequence;

    private String processHistoryTypeCode;

    private String processHistoryTypeCodeName;

    private String remarksContent;

    private String registerDateTime;

    private String registerId;

    private String registerName;

    /**
     * Constructor.
     *
     * @param processHistory {@link ProcessHistory}
     */
    public ProcessHistoryInfo(ProcessHistory processHistory) {
        this.processHistorySequence = processHistory.getProcessHistorySequence();
        this.processVersionSequence = processHistory.getProcessVersionSequence();
        this.processHistoryTypeCode = processHistory.getProcessHistoryTypeCode();
        this.processHistoryTypeCodeName = CodeAndConfigSupport.getCommonCodeName(GroupCode.PROCESS_HISTORY_TYPE_CODE,
                processHistory.getProcessHistoryTypeCode());
        this.remarksContent = processHistory.getRemarksContent();
        this.registerDateTime =
                DateTimeUtils.toString(processHistory.getRegisterDateTime(), DateTimeUtils.FORMAT_FOR_CLIENT);
        this.registerId = processHistory.getRegisterId();
        this.registerName = UserInfoSupport.getUserName(processHistory.getRegisterId());
    }

    public Integer getProcessHistorySequence() {
        return processHistorySequence;
    }

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public String getProcessHistoryTypeCode() {
        return processHistoryTypeCode;
    }

    public String getProcessHistoryTypeCodeName() {
        return processHistoryTypeCodeName;
    }

    public String getRemarksContent() {
        return remarksContent;
    }

    public String getRegisterDateTime() {
        return registerDateTime;
    }

    public String getRegisterId() {
        return registerId;
    }

    public String getRegisterName() {
        return registerName;
    }
}
