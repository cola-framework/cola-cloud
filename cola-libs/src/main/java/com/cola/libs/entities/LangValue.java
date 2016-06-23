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

import org.springframework.data.jpa.repository.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
@Entity
@Table(name = "ref_lang_value", uniqueConstraints = {@UniqueConstraint(columnNames = {"lang_id", "b_id", "table_name", "column_name"})})
public class LangValue extends AbstractEntity {

    @Column(name = "lang_id", nullable = false, unique = true)
    private Long langId;

    @Column(name = "b_id", nullable = false, unique = true)
    private Long bid;

    @Column(name = "table_name", length = 50, nullable = false, unique = true)
    private String tableName;

    @Column(name = "column_name", length = 50, nullable = false, unique = true)
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

    /**
     * Gets lang id.
     * @return the lang id
     */
    public Long getLangId() {
        return langId;
    }

    /**
     * Sets lang id.
     * @param langId the lang id
     */
    public void setLangId(Long langId) {
        this.langId = langId;
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
