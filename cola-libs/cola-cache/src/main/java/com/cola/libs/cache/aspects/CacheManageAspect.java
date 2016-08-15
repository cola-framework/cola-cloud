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
package com.cola.libs.cache.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * cola
 * Created by jiachen.shi on 8/14/2016.
 */
@Aspect
@Configuration
public class CacheManageAspect {

    @Pointcut("@annotation(com.cola.libs.cache.annotations.Cacheable)")
    public void cacheable(){
    }

    @Pointcut("@annotation(com.cola.libs.cache.annotations.CachePut)")
    public void cachePut(){
    }

    @Pointcut("@annotation(com.cola.libs.cache.annotations.CacheEvict)")
    public void cacheEvict(){
    }

    @Order(-1)
    @Around("cacheable()")
    public Object cacheableAspect(ProceedingJoinPoint pjp) throws Throwable {
        return null;
    }

    @Around("cachePut()")
    public Object cachePutAspect(ProceedingJoinPoint pjp) throws Throwable {
        return null;
    }

    @Around("cacheEvict()")
    public Object cacheEvictAspect(ProceedingJoinPoint pjp) throws Throwable {
        return null;
    }

}
