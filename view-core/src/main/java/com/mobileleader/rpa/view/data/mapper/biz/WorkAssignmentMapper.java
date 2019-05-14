package com.mobileleader.rpa.view.data.mapper.biz;

import com.mobileleader.rpa.data.dto.biz.WorkAssignment;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorkAssignmentMapper {
    int insert(WorkAssignment record);

    List<WorkAssignment> selectByWorkSequence(@Param("workSequence") Integer workSequence);

    int updateByPrimaryKey(WorkAssignment record);

    int countByPrimaryKey(@Param("workSequence") Integer workSequence, @Param("robotSequence") Integer robotSequence);
}
