package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.Work;

public interface WorkMapper {

    Work selectByPrimaryKey(Integer workSequence);
}
