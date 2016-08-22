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
package com.cola.libs.jpa.configuration;

import com.cola.libs.jpa.keygen.EntitiesKeyGenerator;
import com.cola.libs.jpa.keygen.JpqlKeyGenerator;
import com.cola.libs.jpa.service.FlexibleSearchService;
import com.cola.libs.jpa.service.ModelService;
import com.cola.libs.jpa.service.impl.FlexibleSearchServiceImpl;
import com.cola.libs.jpa.service.impl.ModelServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

import javax.sql.DataSource;

/**
 * cola
 * Created by jiachen.shi on 8/4/2016.
 */
@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ, order = 2048)
public class JPAConfiguration {

    private static Logger logger = LoggerFactory.getLogger(JPAConfiguration.class);

    @Value("${spring.datasource.type:null}")
    private String dataSourceType;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        Class<? extends DataSource> dataSourceClass = null;
        if(!StringUtils.isEmpty(dataSourceType)){
            try {
                dataSourceClass = (Class<? extends DataSource>)Class.forName(dataSourceType);
            } catch (Exception e) {
                logger.warn("not find Datasource Type:" + e.getMessage(),e);
            }
        }
        return DataSourceBuilder.create().type(dataSourceClass).build();
    }

    @Bean
    public KeyGenerator jpqlKeyGenerator(){
        return new JpqlKeyGenerator();
    }

    @Bean
    public KeyGenerator entitiesKeyGenerator(){
        return new EntitiesKeyGenerator();
    }

    @Bean(name = "modelService")
    @ConditionalOnClass(ModelService.class)
    public ModelService modelService(){
        ModelService modelService = new ModelServiceImpl();
        return modelService;
    }

    @Bean(name = "flexibleSearchService")
    @ConditionalOnClass(FlexibleSearchService.class)
    public FlexibleSearchService flexibleSearchService(){
        FlexibleSearchService flexibleSearchService = new FlexibleSearchServiceImpl();
        return flexibleSearchService;
    }

}
