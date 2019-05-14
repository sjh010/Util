package com.mobileleader.rpa.view.model.form;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

public class WorkAssignmentModifyForm {

    @NotNull
    private Integer workSequence;

    /**
     * 수정하여, 스케줄 정리가 필요한 로봇목록.
     */
    private List<Integer> robotSequenceRemoveList;

    /**
     * 수정하여, 스케줄 생성이 필요한 로봇목록.
     */
    @NotNull
    private List<Integer> robotSequenceAddList;

    /**
     * 팝업에서 선택한 대상 로봇 목록.
     */
    @NotNull
    @NotEmpty
    private List<Integer> robotSequenceList;

    @NotNull
    private Integer processVersionSequence;

    @NotNull
    private String repeatCycleUnitCd;

    private String executeStandardValue;

    @NotNull
    private String execHourMinute;

    @NotNull
    @Pattern(regexp = "Y|N")
    private String workActiveYn;

    public WorkAssignmentModifyForm() {
        super();
    }


    public Integer getWorkSequence() {
        return workSequence;
    }

    public void setWorkSequence(Integer workSequence) {
        this.workSequence = workSequence;
    }

    public List<Integer> getRobotSequenceRemoveList() {
        return robotSequenceRemoveList;
    }

    public void setRobotSequenceRemoveList(List<Integer> robotSequenceRemoveList) {
        this.robotSequenceRemoveList = robotSequenceRemoveList;
    }

    public List<Integer> getRobotSequenceAddList() {
        return robotSequenceAddList;
    }

    public void setRobotSequenceAddList(List<Integer> robotSequenceAddList) {
        this.robotSequenceAddList = robotSequenceAddList;
    }

    public Integer getProcessVersionSequence() {
        return processVersionSequence;
    }

    public void setProcessVersionSequence(Integer processVersionSequence) {
        this.processVersionSequence = processVersionSequence;
    }

    public String getRepeatCycleUnitCd() {
        return repeatCycleUnitCd;
    }

    public void setRepeatCycleUnitCd(String repeatCycleUnitCd) {
        this.repeatCycleUnitCd = repeatCycleUnitCd;
    }

    public String getExecuteStandardValue() {
        return executeStandardValue;
    }

    public void setExecuteStandardValue(String executeStandardValue) {
        this.executeStandardValue = executeStandardValue;
    }

    public String getExecHourMinute() {
        return execHourMinute;
    }

    public void setExecHourMinute(String execHourMinute) {
        this.execHourMinute = execHourMinute;
    }

    public String getWorkActiveYn() {
        return workActiveYn;
    }

    public void setWorkActiveYn(String workActiveYn) {
        this.workActiveYn = workActiveYn;
    }

    public List<Integer> getRobotSequenceList() {
        return robotSequenceList;
    }

    public void setRobotSequenceList(List<Integer> robotSequenceList) {
        this.robotSequenceList = robotSequenceList;
    }
}
