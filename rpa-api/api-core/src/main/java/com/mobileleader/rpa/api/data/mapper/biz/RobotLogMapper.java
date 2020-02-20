package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.RobotLog;

public interface RobotLogMapper {
    int insert(RobotLog record);

    RobotLog selectByPrimaryKey(Integer robotLogSequence);
}
