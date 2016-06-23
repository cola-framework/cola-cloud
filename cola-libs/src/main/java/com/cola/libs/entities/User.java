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
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
@Entity
@Table(name = "t_user")
public class User extends AbstractEntity{

    @Column(length = 20)
    private String name;

    @Column(length = 200)
    private String email;

    @Column(length = 50)
    private String mobile;

    private Date birthday;

    @Column(length = 20)
    private String password;

    @Column(name="provider_id", length = 200)
    private String providerId;

    @Column(name="provider_type", length = 20)
    private String providerType;

    /**
     * Gets name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets mobile.
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets mobile.
     * @param mobile the mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets birthday.
     * @return the birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Sets birthday.
     * @param birthday the birthday
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * Gets password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets provider id.
     * @return the provider id
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * Sets provider id.
     * @param providerId the provider id
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    /**
     * Gets provider type.
     * @return the provider type
     */
    public String getProviderType() {
        return providerType;
    }

    /**
     * Sets provider type.
     * @param providerType the provider type
     */
    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }
}
