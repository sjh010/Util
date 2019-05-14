package com.mobileleader.rpa.view.model.form;

import javax.validation.constraints.NotNull;

public class ProcessHistorySearchForm extends BaseSearchForm {

    // 프로세스 버전 순번
    @NotNull
    private Integer processVersionSequence;

    // 사용 여부
    private String activationYn;

    // 변경 일시
    private String changeDate;

    // 삭제 여부
    private String removeYn;

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public void setProcessVersionSequence(Integer processVersionSequence) {
        this.processVersionSequence = processVersionSequence;
    }

    public String getActivationYn() {
        return activationYn;
    }

    public void setActivationYn(String activationYn) {
        this.activationYn = activationYn;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getRemoveYn() {
        return removeYn;
    }

    public void setRemoveYn(String removeYn) {
        this.removeYn = removeYn;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProcessHistorySearchForm [");
        if (processVersionSequence != null) {
            builder.append("processVersionSequence=").append(processVersionSequence).append(", ");
        }
        if (activationYn != null) {
            builder.append("activationYn=").append(activationYn).append(", ");
        }
        if (removeYn != null) {
            builder.append("removeYn=").append(removeYn);
        }
        builder.append("]");
        return builder.toString();
    }

}
