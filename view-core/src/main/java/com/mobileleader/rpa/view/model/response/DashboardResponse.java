package com.mobileleader.rpa.view.model.response;

import com.mobileleader.rpa.view.data.dto.DashboardProcessExecuteInfo;
import com.mobileleader.rpa.view.data.dto.DashboardProcessResultInfo;
import com.mobileleader.rpa.view.data.dto.DashboardRobotErrorInfo;
import com.mobileleader.rpa.view.data.dto.DashboardRobotInfo;

import java.util.ArrayList;
import java.util.List;

public class DashboardResponse {

    private Integer processCount;

    private Integer activationWorkCount;

    private DashboardRobotInfo robotInfo;

    private List<DashboardProcessExecuteInfo> processList;

    private List<DashboardProcessResultInfo> processResultList;

    private List<DashboardRobotErrorInfo> robotErrorList;

    public Integer getProcessCount() {
        return processCount;
    }

    public void setProcessCount(Integer processCount) {
        this.processCount = processCount != null ? processCount : 0;
    }

    public Integer getActivationWorkCount() {
        return activationWorkCount;
    }

    public void setActivationWorkCount(Integer activationWorkCount) {
        this.activationWorkCount = activationWorkCount != null ? activationWorkCount : 0;
    }

    public DashboardRobotInfo getRobotInfo() {
        return robotInfo;
    }

    public void setRobotInfo(DashboardRobotInfo robotInfo) {
        this.robotInfo = robotInfo != null ? robotInfo : new DashboardRobotInfo();
    }

    public List<DashboardProcessExecuteInfo> getProcessList() {
        return processList;
    }

    public void setProcessList(List<DashboardProcessExecuteInfo> processList) {
        this.processList = processList != null ? processList : new ArrayList<DashboardProcessExecuteInfo>();
    }

    public List<DashboardProcessResultInfo> getProcessResultList() {
        return processResultList;
    }

    public void setProcessResultList(List<DashboardProcessResultInfo> processResultList) {
        this.processResultList = processResultList != null ? processResultList
                : new ArrayList<DashboardProcessResultInfo>();
    }

    public List<DashboardRobotErrorInfo> getRobotErrorList() {
        return robotErrorList;
    }

    public void setRobotErrorList(List<DashboardRobotErrorInfo> robotErrorList) {
        this.robotErrorList = robotErrorList != null ? robotErrorList : new ArrayList<DashboardRobotErrorInfo>();
    }

}
