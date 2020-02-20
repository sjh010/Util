package com.mobileleader.rpa.api.config.schedule;

import com.mobileleader.rpa.api.schedule.listener.GlobalJobDetailListener;
import java.io.IOException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@DependsOn({"dataSource"})
public class ScheduleConfiguration {

    /**
     * Create Quartz SchedulerFactoryBean.
     *
     * @return {@link SchedulerFactoryBean}
     * @throws IOException IOException
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource,
            AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory) throws IOException {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setQuartzProperties(
                PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/quartz-properties.xml")));
        bean.setDataSource(dataSource);
        bean.setWaitForJobsToCompleteOnShutdown(true);
        bean.setGlobalJobListeners(new GlobalJobDetailListener());
        bean.setJobFactory(autowiringSpringBeanJobFactory);
        bean.setAutoStartup(false);
        return bean;
    }

    @Bean
    public AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory() {
        return new AutowiringSpringBeanJobFactory();
    }
}
