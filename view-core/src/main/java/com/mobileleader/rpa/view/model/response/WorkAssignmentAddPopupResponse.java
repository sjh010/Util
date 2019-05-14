package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.ProcessNameVersion;
import com.mobileleader.rpa.view.data.dto.RobotName;
import java.util.List;

/**
 * 업무할당팝업에서 추가시 필요한 응답.
 *
 * @author jkkim
 *
 */
public class WorkAssignmentAddPopupResponse {

    /**
     * 할당되지 않은 로봇 리스트.
     */
    private List<RobotName> robotNameList;

    //
    /**
     * 활성화된 프로세스 리스트.
     */
    private List<ProcessNameVersion> processVersionList;


    public WorkAssignmentAddPopupResponse() {
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
}
