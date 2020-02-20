package com.mobileleader.rpa.api.data.mapper.biz;

import com.mobileleader.rpa.api.aspect.TaskQueueMapperAspect;
import com.mobileleader.rpa.api.data.dto.TaskQueueProcessInfo;
import com.mobileleader.rpa.data.dto.biz.TaskQueue;
import org.apache.ibatis.annotations.Param;

/**
 * Insert/Update/Delete 쿼리 추가 시<br>
 * {@link TaskQueueMapperAspect} StatisticsTaskQueueMapper에도<br>
 * 동일하게 적용되도록 추가해야함.
 */
public interface TaskQueueMapper {

    int insert(TaskQueue record);

    TaskQueue selectByPrimaryKey(Integer taskQueueSequence);

    TaskQueue selectTopByRobotSequenceAndTaskStatusCode(@Param("robotSequence") Integer robotSequence,
            @Param("taskStatusCode") String taskStatusCode);

    int updateTaskStatusCode(@Param("taskQueueSequence") Integer taskQueueSequence,
            @Param("taskStatusCode") String taskStatusCode);

    int updateLastExecuteDateTime(Integer taskQueueSequence);

    TaskQueueProcessInfo selectTaskQueueProcessInfo(Integer taskQueueSequence);
}
