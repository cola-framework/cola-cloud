package com.cola.web.dashboard;

import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore({EurekaServerAutoConfiguration.class})
public class DashboardAutoConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    @Bean
    @ConditionalOnProperty(prefix = "eureka.dashboard", name = "enabled", matchIfMissing = true)
    public DashboardController dashboardController() {
        return new DashboardController(this.applicationInfoManager);
    }

}
