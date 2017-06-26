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
package com.cola.libs.cronjob.entity;

import com.cola.libs.cronjob.beans.CronJobResult;
import com.cola.libs.cronjob.beans.CronJobStatus;
import com.cola.libs.jpa.entity.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
@Entity
@Cacheable
@Table(name = "t_cronjob")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("cronjob")
public class CronJob extends AbstractEntity {

    @Column(name="code", length = 50, nullable = false)
    private String code;

    @Column(name="jobgroup", length = 50, nullable = false)
    private String jobGroup;

    @Column(name="cron_expression", length = 50, nullable = false)
    private String cronExpression;

    @Column(name="spring_id", length = 100, nullable = false)
    private String springId;

    @Column(name="active", nullable = false)
    private Boolean active;

    @Column(name="single_executable")
    private Boolean singleExecutable;

    @Column
    @Enumerated(EnumType.STRING)
    private CronJobStatus cronJobStatus;

    @Column
    @Enumerated(EnumType.STRING)
    private CronJobResult cronJobResult;

    @Column(name="last_execution_time")
    private Date lastExecutionTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getSpringId() {
        return springId;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getSingleExecutable() {
        return singleExecutable;
    }

    public void setSingleExecutable(Boolean singleExecutable) {
        this.singleExecutable = singleExecutable;
    }

    public CronJobStatus getCronJobStatus() {
        return cronJobStatus;
    }

    public void setCronJobStatus(CronJobStatus cronJobStatus) {
        this.cronJobStatus = cronJobStatus;
    }

    public Date getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(Date lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public CronJobResult getCronJobResult() {
        return cronJobResult;
    }

    public void setCronJobResult(CronJobResult cronJobResult) {
        this.cronJobResult = cronJobResult;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
}
