package com.cola.libs.ruleengine.service.impl;

import com.cola.libs.ruleengine.configruation.TestConfiguration;
import com.cola.libs.ruleengine.entity.DroolsKIESession;
import com.cola.libs.ruleengine.service.RuleEngineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiachen.shi on 11/10/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Transactional(readOnly = true)
public class RuleEngineServiceTest {

    @Autowired
    private RuleEngineService ruleEngineService;

    @Test
    public void getDroolsKIESessionByRuleType(){
        String ruleGroup = "promotion";
        DroolsKIESession droolsKIESessionByRuleGroup = ruleEngineService.getDroolsKIESessionByRuleGroup(ruleGroup);
    }

}
