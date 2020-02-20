package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.StudioLog;

public interface StudioLogMapper {
    int insert(StudioLog record);

    StudioLog selectByPrimaryKey(Integer studioLogSequence);
}
