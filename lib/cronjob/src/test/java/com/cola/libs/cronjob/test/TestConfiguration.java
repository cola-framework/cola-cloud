package com.cola.libs.cronjob.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by jiachen.shi on 6/19/2017.
 */
@Configuration
@PropertySource("classpath:test.properties")
@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = {"com.cola.libs"})
public class TestConfiguration {
}
