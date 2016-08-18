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
import javax.persistence.UniqueConstraint;

/**
 * cola
 * Created by jiachen.shi on 8/3/2016.
 */
@Entity
@Table(name = "t_role_lp", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type", "lang_id", "role_id"})}, indexes = {@Index(name="index_role", columnList = "table_type, role_id")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("role_lp")
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "rolelp.language", attributeNodes = @NamedAttributeNode(value = "language")),
        @NamedEntityGraph(name = "rolelp.role", attributeNodes = @NamedAttributeNode(value = "role")),
        @NamedEntityGraph(name = "rolelp.all", attributeNodes = {@NamedAttributeNode(value = "role"), @NamedAttributeNode(value = "language")})})
public class Rolelp extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.REFRESH}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="lang_id", nullable = false)
    private Language language;

    @ManyToOne(cascade = {CascadeType.REFRESH}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    @Column(length = 50)
    private String name;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
