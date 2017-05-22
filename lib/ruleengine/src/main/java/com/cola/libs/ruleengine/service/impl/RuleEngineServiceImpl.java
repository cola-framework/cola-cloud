/*
 * Copyright 2002-${Year} the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cola.libs.ruleengine.service.impl;

import com.cola.libs.jpa.service.FlexibleSearchService;
import com.cola.libs.jpa.support.FlexibleQueryBuilder;
import com.cola.libs.ruleengine.beans.RuleEvaluationContext;
import com.cola.libs.ruleengine.beans.RuleEvaluationResult;
import com.cola.libs.ruleengine.entity.DroolsKIEModule;
import com.cola.libs.ruleengine.entity.DroolsKIESession;
import com.cola.libs.ruleengine.service.RuleEngineService;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.springframework.util.Assert;

import java.util.List;

/**
 * cola
 * Created by jiachen.shi on 8/2/2016.
 */
public class RuleEngineServiceImpl implements RuleEngineService{

    private FlexibleSearchService flexibleSearchService;
    private KieServices kieServices;

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    public KieServices getKieServices() {
        return kieServices;
    }

    public void setKieServices(KieServices kieServices) {
        this.kieServices = kieServices;
    }

    public KieBase getKieBase(){
        KieModuleModel kieModuleModel = this.kieServices.newKieModuleModel();
        KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel("");
        return null;
    }

    protected ReleaseId getReleaseId(DroolsKIEModule module) {
        return this.kieServices.newReleaseId(module.getMvnGroupId(), module.getMvnArtifactId(), module.getMvnVersion());
    }

    protected KieContainer getKieContainer(ReleaseId releaseId){
        return kieServices.newKieContainer(releaseId);
    }

    public DroolsKIESession getDroolsKIESessionByRuleGroup(String ruleGroup){
        FlexibleQueryBuilder queryBuilder = new FlexibleQueryBuilder("from DroolsKIESession kiesession where kiesession.ruleGroup = ?");
        queryBuilder.addParameter(ruleGroup);
        List<DroolsKIESession> result = flexibleSearchService.query(queryBuilder, DroolsKIESession.class);
        if(result != null && result.size() > 0){
            return result.get(0);
        }
        return null;
    }

    @Override
    public RuleEvaluationResult evaluate(RuleEvaluationContext context) {
        Assert.notNull(context, "rule evaluation context must not be null");
        RuleEvaluationResult result = new RuleEvaluationResult();



        return result;
    }

    @Override
    public RuleEvaluationContext getRuleEvaluationContext(String ruleGroup) {
        return null;
    }
}
