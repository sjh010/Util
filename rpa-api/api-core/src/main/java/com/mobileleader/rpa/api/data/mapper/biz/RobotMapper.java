package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.Robot;
import org.apache.ibatis.annotations.Param;

public interface RobotMapper {
    Robot selectByPrimaryKey(Integer robotSequence);

    Robot selectByIpAddressAndPcName(@Param("pcIpAddress") String ipAddress, @Param("pcName") String pcName);

    int updateRobotStatusCode(@Param("robotSequence") Integer robotSequence,
            @Param("robotStatusCode") String robotStatusCode);

    int updateRobotLastExecuteProcessSequence(@Param("robotSequence") Integer robotSequence,
            @Param("processSequence") Integer processSequence);

}
