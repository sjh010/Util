package com.mobileleader.rpa.view.data.dto;

import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;

public class RobotLogStatusCodeInfo {

    private String robotLogStatusCode;

    private String robotLogStatusCodeName;

    public String getRobotLogStatusCode() {
        return robotLogStatusCode;
    }

    /**
     * 로봇 로그 상태 코드 및 이름 설정.
     *
     * @param robotLogStatusCode 로봇 로그 상태 코드
     */
    public void setRobotLogStatusCode(String robotLogStatusCode) {
        this.robotLogStatusCode = robotLogStatusCode;

        this.robotLogStatusCodeName =
                CodeAndConfigSupport.getCommonCodeName(GroupCode.ROBOT_LOG_STATUS_CODE, robotLogStatusCode);
    }

    public String getRobotLogStatusCodeName() {
        return robotLogStatusCodeName;
    }

}
