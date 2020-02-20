package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.ProcessHistory;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProcessHistoryMapper {

    int insert(ProcessHistory record);

    ProcessHistory selectByPrimaryKey(Integer processHistorySequence);

    List<ProcessHistory> selectByProcessVersionSequence(Integer processVersionSequence);

    List<ProcessHistory> selectByProcessVersionSequences(
            @Param("processVersionSequences") List<Integer> processVersionSequences);
}
