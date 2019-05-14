package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.data.dto.biz.Work;
import com.mobileleader.rpa.view.data.dto.ProcessNameVersion;
import com.mobileleader.rpa.view.data.dto.RobotName;
import com.mobileleader.rpa.view.data.dto.WorkAssignedRobot;
import java.util.List;

/**
 * 업무할당팝업에서 추가/수정시 필요한 응답.
 *
 * @author jkkim
 *
 */
public class WorkAssignmentModifyPopupResponse {

    /**
     * 할당되지 않은 로봇 리스트.
     */
    private List<RobotName> robotNameList;

    //
    /**
     * 활성화된 프로세스 리스트.
     */
    private List<ProcessNameVersion> processVersionList;


    /**
     * 기 입력한 로봇 목록.
     */
    private List<WorkAssignedRobot> workAssignedRobotList;

    /**
     * 기 입력한 업무.
     */
    private Work work;

    /**
     * 기 입력한 프로세스/ 버전정보.
     */
    private ProcessNameVersion processNameVersion;

    public WorkAssignmentModifyPopupResponse() {
        super();
    }

    public List<RobotName> getRobotNameList() {
        return robotNameList;
    }

    public void setRobotNameList(List<RobotName> robotNameList) {
        this.robotNameList = robotNameList;
    }

    public List<ProcessNameVersion> getProcessVersionList() {
        return processVersionList;
    }

    public void setProcessVersionList(List<ProcessNameVersion> processVersionList) {
        this.processVersionList = processVersionList;
    }

    public List<WorkAssignedRobot> getWorkAssignedRobotList() {
        return workAssignedRobotList;
    }

    public void setWorkAssignedRobotList(List<WorkAssignedRobot> workAssignedRobotList) {
        this.workAssignedRobotList = workAssignedRobotList;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public ProcessNameVersion getProcessNameVersion() {
        return processNameVersion;
    }

    public void setProcessNameVersion(ProcessNameVersion processNameVersion) {
        this.processNameVersion = processNameVersion;
    }
}
