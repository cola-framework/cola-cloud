package com.cola.lib.ruleengine.configruation;

import com.cola.lib.jpa.service.FlexibleSearchService;
import com.cola.lib.ruleengine.service.RuleEngineService;
import com.cola.lib.ruleengine.service.impl.RuleEngineServiceImpl;
import org.kie.api.KieServices;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by jiachen.shi on 11/9/2016.
 */
@Configuration
public class RuleEngineConfigruation {

    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Bean(name = "ruleEngineService")
    @ConditionalOnClass(RuleEngineService.class)
    public RuleEngineService ruleEngineService(){
        RuleEngineServiceImpl ruleEngineService = new RuleEngineServiceImpl();
        ruleEngineService.setFlexibleSearchService(this.flexibleSearchService);
        ruleEngineService.setKieServices(KieServices.Factory.get());
        return ruleEngineService;
    }

}
