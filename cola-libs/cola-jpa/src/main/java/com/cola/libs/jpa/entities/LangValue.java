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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
@Entity
@Table(name = "ref_lang_value", uniqueConstraints = {@UniqueConstraint(columnNames = {"lang_id", "b_id", "table_name", "column_name"})})
public class LangValue extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name="lang_id", nullable = false)
    private Language language;

    @Column(name = "b_id", nullable = false)
    private Long bid;

    @Column(name = "table_name", length = 50, nullable = false)
    private String tableName;

    @Column(name = "column_name", length = 50, nullable = false)
    private String columnName;

    private String value;

    /**
     * Gets column name.
     * @return the column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets column name.
     * @param columnName the column name
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Gets bid.
     * @return the bid
     */
    public Long getBid() {
        return bid;
    }

    /**
     * Sets bid.
     * @param bid the bid
     */
    public void setBid(Long bid) {
        this.bid = bid;
    }

    /**
     * Gets table name.
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets table name.
     * @param tableName the table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets value.
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
