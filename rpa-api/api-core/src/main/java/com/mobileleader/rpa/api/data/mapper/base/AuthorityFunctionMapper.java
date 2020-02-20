package com.mobileleader.rpa.api.data.mapper.base;

import com.mobileleader.rpa.data.dto.base.AuthorityFunction;
import org.apache.ibatis.annotations.Param;

public interface AuthorityFunctionMapper {
    AuthorityFunction selectByPrimaryKey(@Param("authoritySequence") Short authoritySequence,
            @Param("functionCode") String functionCode);
}
