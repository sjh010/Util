package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.api.model.response.ProcessVersionInfo;
import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import java.util.List;

public interface ProcessVersionMapper {
    ProcessVersion selectByPrimaryKey(Integer processVersionSequence);

    int insert(ProcessVersion record);

    ProcessVersion selectLatestVersionByProcessSequence(Integer processSequence);

    String selectLatestProcessVersion(Integer processSequence);

    List<ProcessVersion> selectByProcessSequence(Integer processSequence);

    List<ProcessVersionInfo> selectProcessVesionWithFileSequenceByProcessSequence(Integer processSequence);
}
