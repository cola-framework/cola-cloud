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
package com.cola.libs.jpa.entity;

import com.cola.libs.jpa.entity.AbstractEntity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * cola
 * Created by jiachen.shi on 7/25/2016.
 */
@Entity
@Cacheable
@Table(name = "t_price_rows", indexes = {@Index(name="index_table_type", columnList = "table_type")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("price_rows")
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "priceRow.product", attributeNodes = @NamedAttributeNode(value = "product"))})
public class PriceRow extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.REFRESH}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
