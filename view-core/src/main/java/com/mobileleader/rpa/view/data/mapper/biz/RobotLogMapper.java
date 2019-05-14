package com.mobileleader.rpa.view.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.RobotLog;
import com.mobileleader.rpa.data.dto.common.CommonCode;
import com.mobileleader.rpa.view.data.dto.RobotLogList;
import com.mobileleader.rpa.view.model.form.RobotLogSearchForm;

import java.util.List;

public interface RobotLogMapper {

    int insert(RobotLog record);

    int selectCount(RobotLogSearchForm form);

    List<RobotLogList> selectRobotLogList(RobotLogSearchForm form);

    List<CommonCode> selectRobotLogTypeCode(Integer robotSequence);

    List<RobotLogList> selectRobotLogListForExcel(RobotLogSearchForm form);
}
