package com.mobileleader.rpa.view.data.dto;

import com.mobileleader.rpa.data.type.GroupCode;
import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;

import org.joda.time.DateTime;

public class RobotList {

    private Integer robotSequence;

    private String robotName;

    private String pcIpAddress;

    private String pcName;

    private String robotStatusCode;

    private String robotStatusCodeName;

    private DateTime lastActionDateTime;

    private Integer lastExecuteProcessSequence;

    private String processName;

    public Integer getRobotSequence() {
        return robotSequence;
    }

    public void setRobotSequence(Integer robotSequence) {
        this.robotSequence = robotSequence;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getPcIpAddress() {
        return pcIpAddress;
    }

    public void setPcIpAddress(String pcIpAddress) {
        this.pcIpAddress = pcIpAddress;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public String getRobotStatusCode() {
        return robotStatusCode;
    }

    public void setRobotStatusCode(String robotStatusCode) {
        this.robotStatusCode = robotStatusCode;
        this.robotStatusCodeName = CodeAndConfigSupport.getCommonCodeName(GroupCode.ROBOT_STATUS_CODE, robotStatusCode);
    }

    public DateTime getLastActionDateTime() {
        return lastActionDateTime;
    }

    public void setLastActionDateTime(DateTime lastActionDateTime) {
        this.lastActionDateTime = lastActionDateTime;
    }

    public Integer getLastExecuteProcessSequence() {
        return lastExecuteProcessSequence;
    }

    public void setLastExecuteProcessSequence(Integer lastExecuteProcessSequence) {
        this.lastExecuteProcessSequence = lastExecuteProcessSequence;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getRobotStatusCodeName() {
        return robotStatusCodeName;
    }

    public void setRobotStatusCodeName(String robotStatusCodeName) {
        this.robotStatusCodeName = robotStatusCodeName;
    }

}
