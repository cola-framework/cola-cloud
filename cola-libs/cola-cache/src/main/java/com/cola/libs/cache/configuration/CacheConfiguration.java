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
package com.cola.libs.cache.configuration;

import com.cola.libs.cache.management.CacheManagerFactory;
import com.cola.libs.cache.management.IgniteCacheManager;
import com.cola.libs.cache.management.RedisCacheManager;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.hibernate.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * cola
 * Created by jiachen.shi on 8/12/2016.
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CacheConfiguration extends HibernateJpaAutoConfiguration{

    private static Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);

    @Value("${spring.redis.expire:0}")
    private Long expiration;

    @Value("${spring.ignite.gridname:null}")
    private String igniteGridName;

    @Value("${spring.ignite.entity.packages:com.cola}")
    private String igniteEntityPackages;

    private org.apache.ignite.configuration.CacheConfiguration[] initIgniteCacheConfig(){

        List<org.apache.ignite.configuration.CacheConfiguration> list = new ArrayList<>();

        org.apache.ignite.configuration.CacheConfiguration cacheConfiguration = new org.apache.ignite.configuration.CacheConfiguration();
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheConfiguration.setName("org.hibernate.cache.spi.UpdateTimestampsCache");
        list.add(cacheConfiguration);

        cacheConfiguration = new org.apache.ignite.configuration.CacheConfiguration();
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheConfiguration.setName("org.hibernate.cache.internal.StandardQueryCache");
        list.add(cacheConfiguration);

        String[] entityPackages = StringHelper.split(" \n\r\f\t,;", igniteEntityPackages, false);
        DefaultPersistenceUnitManager internalPersistenceUnitManager = new DefaultPersistenceUnitManager();
        internalPersistenceUnitManager.setPackagesToScan(entityPackages);
        internalPersistenceUnitManager.preparePersistenceUnitInfos();
        List<String> managedClassNames = internalPersistenceUnitManager.obtainDefaultPersistenceUnitInfo().getManagedClassNames();
        for(String entityClassName:managedClassNames){
            cacheConfiguration = new org.apache.ignite.configuration.CacheConfiguration();
            cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
            cacheConfiguration.setName(entityClassName);
            list.add(cacheConfiguration);
        }

        return list.toArray(new org.apache.ignite.configuration.CacheConfiguration[list.size()]);
    }

    @Bean
    @Primary
    @DependsOn("cacheManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder factoryBuilder, DataSource dataSource) {
        Map<String, Object> vendorProperties = getVendorProperties();
        customizeVendorProperties(vendorProperties);
        return factoryBuilder.dataSource(dataSource).packages(getPackagesToScan())
                .properties(vendorProperties).jta(isJta()).build();
    }

    @Bean
    @ConditionalOnClass(CacheManagerFactory.class)
    public CacheManagerFactory cacheManagerFactory(CacheManager cacheManager){
        CacheManagerFactory cacheManagerFactory = new CacheManagerFactory();
        cacheManagerFactory.setCacheManager(cacheManager);
        return cacheManagerFactory;
    }

    @Bean
    @ConditionalOnClass(CacheManager.class)
    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
    public CacheManager redisCacheManager(RedisTemplate<?,?> redisTemplate) {
        CacheManager cacheManager = new RedisCacheManager(redisTemplate, expiration);
        return cacheManager;
    }

    @Bean
    @ConditionalOnClass(CacheManager.class)
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "ignite", matchIfMissing = true)
    public CacheManager igniteCacheManager(IgniteConfiguration igniteConfiguration) {
        IgniteCacheManager cacheManager = new IgniteCacheManager();
        cacheManager.setGridName(this.igniteGridName);
        igniteConfiguration.setGridLogger(new Slf4jLogger(logger));
        igniteConfiguration.setGridName(this.igniteGridName);
        igniteConfiguration.setCacheConfiguration(initIgniteCacheConfig());
        cacheManager.setConfiguration(igniteConfiguration);
        return cacheManager;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.ignite")
    public IgniteConfiguration igniteConfiguration(){
        return new IgniteConfiguration();
    }

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
