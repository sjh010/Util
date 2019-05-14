package com.mobileleader.rpa.view.model.form;

import com.mobileleader.rpa.view.query.QueryBuilder;

public class ProcessSearchForm extends BaseSearchForm {

    // 프로세스명
    private String processName;

    // 형상관리코드
    private String configManagementStatusCode;

    // 상태코드
    private String activationYn;

    public ProcessSearchForm() {
        super();
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getConfigManagementStatusCode() {
        return configManagementStatusCode;
    }

    public void setConfigManagementStatusCode(String configManagementStatusCode) {
        this.configManagementStatusCode = configManagementStatusCode;
    }

    public String getActivationYn() {
        return activationYn;
    }

    public void setActivationYn(String activationYn) {
        this.activationYn = activationYn;
    }

    public String getProcessNameQuery() {
        return QueryBuilder.getWhereLikeClause("prcs.PRCS_NM", this.processName);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProcessSearchForm [");
        if (processName != null) {
            builder.append("processName=").append(processName).append(", ");
        }
        if (configManagementStatusCode != null) {
            builder.append("configManagementStatusCode=").append(configManagementStatusCode).append(", ");
        }
        if (activationYn != null) {
            builder.append("activationYn=").append(activationYn);
        }
        builder.append("]");
        return builder.toString();
    }

}
