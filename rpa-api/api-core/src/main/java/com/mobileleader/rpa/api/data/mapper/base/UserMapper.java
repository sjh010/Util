package com.mobileleader.rpa.api.data.mapper.base;

import com.mobileleader.rpa.data.dto.base.User;

public interface UserMapper {
    User selectByPrimaryKey(Integer userSequence);

    Integer selectUserSequenceByUserId(String userId);
}
