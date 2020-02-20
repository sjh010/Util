package com.mobileleader.rpa.api.model.response;

import com.mobileleader.rpa.data.dto.biz.Process;
import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;
import com.mobileleader.rpa.repository.user.UserInfoSupport;
import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import java.util.List;

public class ProcessInfo {

    private Integer processSequence;

    private String processName;

    private String configManagementStatusCode;

    private String configManagementStatusCodeName;

    private String configManagementStatusModifyDateTime;

    private String configManagementUserId;

    private String configManagementUserName;

    private String registerId;

    private String registerName;

    private List<ProcessVersionInfo> processVersionInfoList;

    /**
     * Constructor.
     *
     * @param process {@link Process}
     * @param processVersionInfoList processVersion list
     */
    public ProcessInfo(Process process, List<ProcessVersionInfo> processVersionInfoList) {
        this.processSequence = process.getProcessSequence();
        this.processName = process.getProcessName();
        this.configManagementStatusCode = process.getConfigManagementStatusCode();
        this.configManagementStatusCodeName = CodeAndConfigSupport.getCommonCodeName(
                GroupCode.CONFIGURATION_MANAGEMENT_STATUS_CODE, process.getConfigManagementStatusCode());
        this.configManagementStatusModifyDateTime = DateTimeUtils
                .toString(process.getConfigManagementStatusModifyDateTime(), DateTimeUtils.FORMAT_FOR_CLIENT);
        this.configManagementUserId = process.getConfigManagementUserId();
        this.configManagementUserName = UserInfoSupport.getUserName(process.getConfigManagementUserId());
        this.registerId = process.getRegisterId();
        this.registerName = UserInfoSupport.getUserName(process.getRegisterId());
        this.processVersionInfoList = processVersionInfoList;
    }

    public Integer getProcessSequence() {
        return processSequence;
    }

    public String getProcessName() {
        return processName;
    }

    public String getConfigManagementStatusCode() {
        return configManagementStatusCode;
    }

    public String getConfigManagementStatusCodeName() {
        return configManagementStatusCodeName;
    }

    public String getConfigManagementStatusModifyDateTime() {
        return configManagementStatusModifyDateTime;
    }

    public String getRegisterId() {
        return registerId;
    }

    public String getRegisterName() {
        return registerName;
    }

    public List<ProcessVersionInfo> getProcessVersionInfoList() {
        return processVersionInfoList;
    }

    public String getConfigManagementUserId() {
        return configManagementUserId;
    }

    public String getConfigManagementUserName() {
        return configManagementUserName;
    }
}
