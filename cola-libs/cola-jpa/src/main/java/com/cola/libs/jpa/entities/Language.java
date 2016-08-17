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

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
@Entity
@Table(name = "t_language", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type", "iso_code"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("language")
public class Language extends AbstractEntity{

    @Column(name="iso_code", length = 20, nullable = false)
    private String isoCode;

    /**
     * Gets iso code.
     * @return the iso code
     */
    public String getIsoCode() {
        return isoCode;
    }

    /**
     * Sets iso code.
     * @param isoCode the iso code
     */
    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }
}
