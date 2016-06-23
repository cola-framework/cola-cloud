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
package com.cola.libs.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "modify_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiTime;

    @Column(name = "create_by", nullable = false)
    private Long createBy;

    @Column(name = "modify_by", nullable = false)
    private Long modifiBy;

    @Column(name = "up_times", nullable = false)
    private Long updateTimes = 0L;

    /**
     * Gets id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets create time.
     * @return the create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     * @param createTime the create time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets modifi time.
     * @return the modifi time
     */
    public Date getModifiTime() {
        return modifiTime;
    }

    /**
     * Sets modifi time.
     * @param modifiTime the modifi time
     */
    public void setModifiTime(Date modifiTime) {
        this.modifiTime = modifiTime;
    }

    /**
     * Gets create by.
     * @return the create by
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * Sets create by.
     * @param createBy the create by
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * Gets modifi by.
     * @return the modifi by
     */
    public Long getModifiBy() {
        return modifiBy;
    }

    /**
     * Sets modifi by.
     * @param modifiBy the modifi by
     */
    public void setModifiBy(Long modifiBy) {
        this.modifiBy = modifiBy;
    }

    /**
     * Gets update times.
     * @return the update times
     */
    public Long getUpdateTimes() {
        return updateTimes;
    }

    /**
     * Sets update times.
     * @param updateTimes the update times
     */
    public void setUpdateTimes(Long updateTimes) {
        this.updateTimes = updateTimes;
    }
}
