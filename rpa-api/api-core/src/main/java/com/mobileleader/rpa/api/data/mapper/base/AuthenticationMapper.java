package com.mobileleader.rpa.api.data.mapper.base;

import com.mobileleader.rpa.data.dto.base.Authentication;
import org.apache.ibatis.annotations.Param;

public interface AuthenticationMapper {
    int deleteByPrimaryKey(@Param("authenticationTypeCode") String authenticationTypeCode,
            @Param("authenticationTargetSequence") Integer authenticationTargetSequence);

    int deleteByUuid(@Param("authenticationTypeCode") String authenticationTypeCode, @Param("uuid") String uuid);

    int insert(Authentication record);

    Authentication selectByPrimaryKey(@Param("authenticationTypeCode") String authenticationTypeCode,
            @Param("authenticationTargetSequence") Integer authenticationTargetSequence);

    int updateAuthenticationTypeCodeByPrimaryKey(@Param("authenticationTypeCode") String authenticationTypeCode,
            @Param("authenticationTargetSequence") Integer authenticationTargetSequence,
            @Param("updateAuthenticationTypeCode") String updateAuthenticationTypeCode);
}
