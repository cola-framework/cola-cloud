package com.cola.lib.ruleengine.service;

import com.cola.lib.ruleengine.beans.RuleEvaluationContext;
import com.cola.lib.ruleengine.beans.RuleEvaluationResult;
import com.cola.lib.ruleengine.entity.DroolsKIESession;

/**
 * Created by jiachen.shi on 11/9/2016.
 */
public interface RuleEngineService{

    RuleEvaluationResult evaluate(RuleEvaluationContext var1);

    RuleEvaluationContext getRuleEvaluationContext(String ruleGroup);

    DroolsKIESession getDroolsKIESessionByRuleGroup(String ruleGroup);

}
