package com.mobileleader.rpa.view.model.form;

import com.mobileleader.rpa.view.query.QueryBuilder;

public class WorkAssignmentSearchForm extends BaseSearchForm {

    private String processName;

    private String registerDateTime;

    private String activationYn;

    public WorkAssignmentSearchForm() {
        setSortOrder("DESC");
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(String registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public String getactivationYn() {
        return activationYn;
    }

    public void setActivationYn(String activationYn) {
        this.activationYn = activationYn;
    }

    public String getProcessNameQuery() {
        return QueryBuilder.getWhereLikeClause("P.PRCS_NM", this.processName);
    }

}
