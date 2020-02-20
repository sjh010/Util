package com.mobileleader.rpa.api.data.mapper.statistics;

import com.mobileleader.rpa.api.aspect.TaskQueueMapperAspect;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;

/**
 * Mapper 직접 사용 금지.<br>
 * {@link TaskQueueMapperAspect}를 통해서 사용
 */
public interface StatisticsTaskQueueMapper {
    int insert(Integer taskQueueSequence);

    int updateTaskStatusCode(Integer taskQueueSequence);

    int updateLastExecuteDateTime(Integer taskQueueSequence);

    int deleteByLastExecuteDateTimePeriods(@Param("afterDateTime") DateTime afterDateTime,
            @Param("beforeDateTime") DateTime beforeDateTime);
}
