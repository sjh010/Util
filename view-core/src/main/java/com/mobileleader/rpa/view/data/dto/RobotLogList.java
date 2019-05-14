package com.mobileleader.rpa.view.data.dto;

import com.mobileleader.rpa.data.dto.biz.RobotLog;
import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;

public class RobotLogList extends RobotLog {

    private String robotLogTypeCodeName;

    private String robotLogStatusCodeName;

    @Override
    public void setRobotLogTypeCode(String robotLogTypeCode) {
        super.setRobotLogTypeCode(robotLogTypeCode);
        robotLogTypeCodeName = CodeAndConfigSupport.getCommonCodeName(GroupCode.ROBOT_LOG_TYPE_CODE, robotLogTypeCode);
    }

    @Override
    public void setRobotLogStatusCode(String robotLogStatusCode) {
        super.setRobotLogStatusCode(robotLogStatusCode);
        robotLogStatusCodeName = CodeAndConfigSupport.getCommonCodeName(GroupCode.ROBOT_LOG_STATUS_CODE,
                robotLogStatusCode);
    }

    public String getRobotLogTypeCodeName() {
        return robotLogTypeCodeName;
    }

    public String getRobotLogStatusCodeName() {
        return robotLogStatusCodeName;
    }

}
