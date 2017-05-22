package com.cola.libs.ruleengine.service;

import com.cola.libs.ruleengine.beans.RuleEvaluationContext;
import com.cola.libs.ruleengine.beans.RuleEvaluationResult;
import com.cola.libs.ruleengine.entity.DroolsKIESession;

/**
 * Created by jiachen.shi on 11/9/2016.
 */
public interface RuleEngineService{

    RuleEvaluationResult evaluate(RuleEvaluationContext var1);

    RuleEvaluationContext getRuleEvaluationContext(String ruleGroup);

    DroolsKIESession getDroolsKIESessionByRuleGroup(String ruleGroup);

}
