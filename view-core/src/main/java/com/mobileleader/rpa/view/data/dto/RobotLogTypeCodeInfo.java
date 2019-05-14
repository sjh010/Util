package com.mobileleader.rpa.view.data.dto;

import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;

public class RobotLogTypeCodeInfo {

    private String robotLogTypeCode;

    private String robotLogTypeCodeName;

    public String getRobotLogTypeCode() {
        return robotLogTypeCode;
    }

    /**
     * 로봇 로그 유형 코드 및 이름 설정.
     *
     * @param robotLogTypeCode 로봇 로그 유형 코드
     */
    public void setRobotLogTypeCode(String robotLogTypeCode) {
        this.robotLogTypeCode = robotLogTypeCode;

        this.robotLogTypeCodeName =
                CodeAndConfigSupport.getCommonCodeName(GroupCode.ROBOT_LOG_TYPE_CODE, robotLogTypeCode);
    }

    public String getRobotLogTypeCodeName() {
        return robotLogTypeCodeName;
    }

}
