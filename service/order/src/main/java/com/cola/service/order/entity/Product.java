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
package com.cola.service.order.entity;

import com.cola.lib.jpa.entity.AbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
@Entity
@Cacheable
@Table(name = "t_product", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type","code"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("product")
@NamedEntityGraphs(value = {@NamedEntityGraph(name = "product.priceRows", attributeNodes = @NamedAttributeNode("priceRows"))})
public class Product extends AbstractEntity {

    @Column(length = 20, nullable = false)
    private String code;

    /**
     * Gets code.
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

}
