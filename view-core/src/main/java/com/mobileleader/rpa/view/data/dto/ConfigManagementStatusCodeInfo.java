package com.mobileleader.rpa.view.data.dto;

import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;

public class ConfigManagementStatusCodeInfo {

    private String configManagementStatusCode;

    private String configManagementStatusCodeName;

    public String getConfigManagementStatusCode() {
        return configManagementStatusCode;
    }

    /**
     * 형상 관리 코드 및 코드 이름 설정.
     *
     * @param configManagementStatusCode 형상 관리 코드
     */
    public void setConfigManagementStatusCode(String configManagementStatusCode) {
        this.configManagementStatusCode = configManagementStatusCode;
        this.configManagementStatusCodeName = CodeAndConfigSupport
                .getCommonCodeName(GroupCode.CONFIGURATION_MANAGEMENT_STATUS_CODE, configManagementStatusCode);
    }

    public String getConfigManagementStatusCodeName() {
        return configManagementStatusCodeName;
    }
}
