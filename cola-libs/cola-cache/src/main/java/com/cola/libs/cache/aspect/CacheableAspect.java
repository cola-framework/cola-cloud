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
package com.cola.libs.cache.aspect;

import com.cola.libs.cache.annotation.Cacheable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * cola
 * Created by jiachen.shi on 8/17/2016.
 */
@Aspect
@Order(-10)
@Configuration
public class CacheableAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.cola.libs.cache.annotation.Cacheable)")
    public void cacheable() {
    }

    @Around("cacheable()")
    public Object aspect(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] arguments = joinPoint.getArgs();
        Method proxyMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Method soruceMethod = joinPoint.getTarget().getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());

        Cacheable annotation = soruceMethod.getAnnotation(Cacheable.class);
        String cacheManagerName = annotation.cacheManager();
        CacheManager cacheManager = null;
        Cache cache = null;
        if(!StringUtils.isEmpty(cacheManagerName)){
            cacheManager = this.applicationContext.getBean(cacheManagerName, CacheManager.class);
        }else{
            cacheManager = this.applicationContext.getBean(CacheManager.class);
        }

        if(cacheManager != null){
            cache = cacheManager.getCache(annotation.value());
        }

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        ParameterNameDiscoverer pnd=new DefaultParameterNameDiscoverer();
        String[] parameterNames=pnd.getParameterNames(soruceMethod);
        int i=0;
        for(String pName:parameterNames){
            context.setVariable(pName, arguments[i]);
            i++;
        }

        Object key = null;
        String keyGenerator = annotation.keyGenerator();
        if(!StringUtils.isEmpty(keyGenerator)){
            KeyGenerator bean = this.applicationContext.getBean(keyGenerator, KeyGenerator.class);
            key = bean.generate(joinPoint.getTarget(), soruceMethod, arguments);
        }else{
            String keyExpression = annotation.key();
            if(!StringUtils.isEmpty(keyExpression)){
                key = parser.parseExpression(keyExpression).getValue(context, Object.class);
            }
        }

        boolean condition = true;
        String conditionExpression = annotation.condition();
        if(!StringUtils.isEmpty(conditionExpression)){
            condition = parser.parseExpression(conditionExpression).getValue(context, boolean.class);
        }

        if(cache != null && key != null && condition){
            Object o = cache.get(key, soruceMethod.getReturnType());
            if(o != null){
                return o;
            }
        }

        Object result = joinPoint.proceed();

        boolean unless = true;
        String unlessExpression = annotation.unless();
        if(!StringUtils.isEmpty(unlessExpression)){
            context.setVariable("result", result);
            unless = parser.parseExpression(unlessExpression).getValue(context, boolean.class);
        }
        if(cache != null && !unless){
            cache.put(key, result);
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
