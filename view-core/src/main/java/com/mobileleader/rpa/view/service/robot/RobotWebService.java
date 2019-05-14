package com.mobileleader.rpa.view.service.robot;

import com.mobileleader.rpa.view.model.form.RobotAddForm;
import com.mobileleader.rpa.view.model.form.RobotLogSearchForm;
import com.mobileleader.rpa.view.model.form.RobotSearchForm;
import com.mobileleader.rpa.view.model.response.RobotAddCheckResponse;
import com.mobileleader.rpa.view.model.response.RobotListResponse;
import com.mobileleader.rpa.view.model.response.RobotLogFileterResponse;
import com.mobileleader.rpa.view.model.response.RobotLogListResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RobotWebService {

    public RobotListResponse getRobotList(RobotSearchForm form);

    public List<String> getPcNameList();

    public int addRobot(RobotAddForm form);

    public int deleteRobot(List<Integer> robotSequenceList);

    public RobotLogListResponse getRobotLogList(RobotLogSearchForm form);

    public RobotLogFileterResponse getRobotLogFilterList(Integer robotSequence);

    public String createRobotLogExcel(RobotLogSearchForm form);

    public void downloadExcel(String uuid, HttpServletRequest request, HttpServletResponse response);

    public void downloadErrorLogFile(Integer fileGroupSequence, String robotName, HttpServletRequest request,
            HttpServletResponse response);

    public RobotAddCheckResponse checkRobotAddValidity(RobotAddForm form);

}
