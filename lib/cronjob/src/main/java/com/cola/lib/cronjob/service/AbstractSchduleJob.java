package com.cola.lib.cronjob.service;

import com.cola.lib.cronjob.entity.CronJob;
import com.cola.lib.jpa.service.FlexibleSearchService;
import com.cola.lib.jpa.service.ModelService;
import org.quartz.Job;

/**
 * Created by jiachen.shi on 6/15/2017.
 */
public abstract class AbstractSchduleJob<T extends CronJob> implements Job{

    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
