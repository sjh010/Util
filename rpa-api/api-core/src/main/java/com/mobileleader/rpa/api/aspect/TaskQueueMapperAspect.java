package com.mobileleader.rpa.api.aspect;

import com.mobileleader.rpa.api.data.mapper.statistics.StatisticsTaskQueueMapper;
import com.mobileleader.rpa.data.dto.biz.TaskQueue;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TaskQueueMapperAspect {
    private static final Logger logger = LoggerFactory.getLogger(TaskQueueMapperAspect.class);

    @Autowired
    private StatisticsTaskQueueMapper statisticsTaskQueueMapper;

    /**
     * StatisticsTaskQueue Insert after TaskQueueInsert.
     *
     * @param joinPoint {@link JoinPoint}
     * @param result insert count
     * @return
     */
    @AfterReturning(pointcut = "execution(* com.mobileleader.rpa.api.data.mapper.biz.TaskQueueMapper.insert(..))",
            returning = "result")
    public int insert(JoinPoint joinPoint, int result) {
        if (result > 0) {
            try {
                TaskQueue insertedTaskQueue = (TaskQueue) joinPoint.getArgs()[0];
                statisticsTaskQueueMapper.insert(insertedTaskQueue.getTaskQueueSequence());
            } catch (RuntimeException e) {
                logger.error("[StatisticsTaskQueueMapper Exception]", e);
            }
        }
        return result;
    }

    /**
     * StatisticsTaskQueueStatusCode Update after TaskQueueUpdateTaskStatusCode.
     *
     * @param joinPoint {@link JoinPoint}
     * @param result update count
     * @return
     */
    // @formatter:off
    @AfterReturning(
        pointcut =
        "execution(* com.mobileleader.rpa.api.data.mapper.biz.TaskQueueMapper.updateTaskStatusCode(..))",
        returning = "result")
    // @formatter:on
    public int updateTaskStatusCode(JoinPoint joinPoint, int result) {
        if (result > 0) {
            try {
                statisticsTaskQueueMapper.updateTaskStatusCode((Integer) joinPoint.getArgs()[0]);
            } catch (RuntimeException e) {
                logger.error("[StatisticsTaskQueueMapper Exception]", e);
            }
        }
        return result;
    }

    /**
     * StatisticsTaskQueueLastExecuteDateTime Update after TaskQueueUpdateLastExecuteDateTime.
     *
     * @param joinPoint {@link JoinPoint}
     * @param result update count
     * @return
     */
    // @formatter:off
    @AfterReturning(
        pointcut =
        "execution(* com.mobileleader.rpa.api.data.mapper.biz.TaskQueueMapper.updateLastExecuteDateTime(..))",
        returning = "result")
    // @formatter:on
    public int updateLastExecuteDateTime(JoinPoint joinPoint, int result) {
        if (result > 0) {
            try {
                statisticsTaskQueueMapper.updateLastExecuteDateTime((Integer) joinPoint.getArgs()[0]);
            } catch (RuntimeException e) {
                logger.error("[StatisticsTaskQueueMapper Exception]", e);
            }
        }
        return result;
    }
}
