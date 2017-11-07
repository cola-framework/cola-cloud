package com.cola.web.dashboard.configuratioin;

import com.cola.web.dashboard.controller.DashboardController;
import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.PostConstruct;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore({EurekaServerAutoConfiguration.class})
public class DashboardAutoConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    protected FreeMarkerViewResolver resolver;
    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    @PostConstruct
    public void setFreemarkerVariable(){
        resolver.setCache(false);
        resolver.setSuffix(".html");
        resolver.setContentType("text/html; charset=UTF-8");
    }

    @Bean
    @ConditionalOnProperty(prefix = "cola.dashboard", name = "enabled", matchIfMissing = true)
    public DashboardController dashboardController() {
        return new DashboardController(this.applicationInfoManager);
    }
}
