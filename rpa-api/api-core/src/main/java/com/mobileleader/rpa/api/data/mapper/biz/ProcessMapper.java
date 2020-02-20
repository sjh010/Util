package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.Process;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProcessMapper {
    int insert(Process record);

    Process selectByPrimaryKey(Integer processSequence);

    List<Process> selectAll();

    int updateProcessConfigManagementStatus(@Param("processSequence") Integer processSequence,
            @Param("configManagementStatusCode") String configManagementStatusCode,
            @Param("configManagementUserId") String configManagementUserId);

    int countByProcessName(String processName);
}
