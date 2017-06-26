package com.cola.libs.cronjob.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by jiachen.shi on 6/16/2017.
 */
@Configuration
public class ScheduledConfiguration {

    @Bean
    @ConditionalOnClass(SchedulerFactoryBean.class)
    public SchedulerFactoryBean schedulerFactory(){
        SchedulerFactoryBean bean = new SchedulerFactoryBean ();
        return bean;
    }

}
