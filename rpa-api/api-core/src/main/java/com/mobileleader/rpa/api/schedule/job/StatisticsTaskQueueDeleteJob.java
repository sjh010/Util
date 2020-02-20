package com.mobileleader.rpa.api.schedule.job;

import com.mobileleader.rpa.api.data.mapper.statistics.StatisticsTaskQueueMapper;
import com.mobileleader.rpa.utils.datetime.DateTimeUtils;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticsTaskQueueDeleteJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsTaskQueueDeleteJob.class);

    @Autowired
    private StatisticsTaskQueueMapper statisticsTaskQueueMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        DateTime afterDateTime = DateTimeUtils.now().withMillis(0);
        DateTime beforeDateTime = DateTimeUtils.now().minusDays(2).withHourOfDay(0).withMinuteOfHour(0)
                .withSecondOfMinute(0).withMillisOfSecond(0);
        int count = statisticsTaskQueueMapper.deleteByLastExecuteDateTimePeriods(afterDateTime, beforeDateTime);
        logger.info("[deleteByLastExecuteDateTimePeriods] afterDateTime : {}, beforeDateTime : {}, deletedCount : {}",
                afterDateTime.toString(), beforeDateTime.toString(), count);
    }
}
