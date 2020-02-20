package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.WorkAssignment;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorkAssignmentMapper {
    List<WorkAssignment> selectByWorkSequence(@Param("workSequence") Integer workSequence);
}
