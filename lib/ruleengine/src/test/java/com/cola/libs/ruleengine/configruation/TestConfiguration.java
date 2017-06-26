package com.cola.libs.ruleengine.configruation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by jiachen.shi on 11/10/2016.
 */
@Configuration
@EnableAutoConfiguration(exclude={FallbackWebSecurityAutoConfiguration.class})
@PropertySource("classpath:hikariCP.properties")
@EntityScan(basePackages = {"com.cola.libs"})
public class TestConfiguration {
}
