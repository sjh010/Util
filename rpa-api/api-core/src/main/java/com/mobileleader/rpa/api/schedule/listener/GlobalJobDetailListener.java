package com.mobileleader.rpa.api.schedule.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalJobDetailListener implements JobListener {
    private static final Logger logger = LoggerFactory.getLogger(GlobalJobDetailListener.class);

    private static final String LISTENER_NAME = "globalJobDetailListener";

    @Override
    public String getName() {
        return LISTENER_NAME;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.info("[jobToBeExecuted] jobKey : {}, triggerKey : {}", context.getJobDetail().getKey(),
                context.getTrigger().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        logger.info("[jobWasExecuted] jobKey : {}, triggerKey : {}", context.getJobDetail().getKey(),
                context.getTrigger().getKey());
    }
}
