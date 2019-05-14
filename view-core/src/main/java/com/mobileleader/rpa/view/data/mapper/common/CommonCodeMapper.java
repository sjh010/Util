package com.mobileleader.rpa.view.data.mapper.common;

import com.mobileleader.rpa.data.dto.common.CommonCode;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommonCodeMapper {
    CommonCode selectByPrimaryKey(@Param("groupCode") String groupCode, @Param("code") String code);

    List<CommonCode> selectByGroupCode(@Param("groupCode") String groupCode);
}
