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
package com.cola.libs.jpa.entities;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
@MappedSuperclass
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @LastModifiedDate
    @Column(name = "modify_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedTime;

    @CreatedBy
    @Column(name = "create_by", nullable = false)
    private Long createBy;

    @LastModifiedBy
    @Column(name = "modify_by", nullable = false)
    private Long lastModifiedBy;

    @Version
    private Long version;

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
     * Gets version.
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets version.
     * @param version the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Gets last modified time.
     * @return the last modified time
     */
    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     * Sets last modified time.
     * @param lastModifiedTime the last modified time
     */
    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    /**
     * Gets last modified by.
     * @return the last modified by
     */
    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Sets last modified by.
     * @param lastModifiedBy the last modified by
     */
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
