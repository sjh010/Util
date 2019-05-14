package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.data.dto.common.CommonCode;

import java.util.List;

public class RobotLogFileterResponse {

    List<CommonCode> robotLogTypeList;

    List<CommonCode> robotLogStatusList;

    public List<CommonCode> getRobotLogTypeList() {
        return robotLogTypeList;
    }

    public void setRobotLogTypeList(List<CommonCode> robotLogTypeList) {
        this.robotLogTypeList = robotLogTypeList;
    }

    public List<CommonCode> getRobotLogStatusList() {
        return robotLogStatusList;
    }

    public void setRobotLogStatusList(List<CommonCode> robotLogStatusList) {
        this.robotLogStatusList = robotLogStatusList;
    }

}
