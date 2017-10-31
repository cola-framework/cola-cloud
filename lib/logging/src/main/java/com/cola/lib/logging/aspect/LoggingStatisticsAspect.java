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
package com.cola.lib.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * cola
 * Created by jiachen.shi on 9/6/2016.
 */
@Aspect
@DeclarePrecedence("com.cola.libs.jpa.aspect.RetryOnOptimisticLockingFailureAspect,com.cola.libs.logging.aspect.LoggingStatisticsAspect,org.springframework.transaction.aspectj.AnnotationTransactionAspect")
public class LoggingStatisticsAspect {

    private final String DEFAULT_PACKAGE = "com.cola";

    private static Logger logger;

    @Pointcut("execution(* com.cola..*.*(..))")
    public void includedPackages() {
    }

    @Pointcut("execution(* com.cola..test..*.*(..))")
    public void excluedTests(){
    }

    @Pointcut("execution(* com.cola.libs.logging..*.*(..))")
    public void excludedPackages() {
    }

    @Pointcut("@within(org.springframework.boot.autoconfigure.SpringBootApplication)")
    public void excludedApplicationRunner() {
    }

    @Pointcut("@within(org.springframework.context.annotation.Configuration) || execution(* com.cola..configuration..*.*(..))")
    public void excludedConfiguration() {
    }

/*    @Pointcut("@within(javax.persistence.Entity) || @within(javax.persistence.MappedSuperclass) || execution(* (com.cola.libs.jpa.entity.AbstractEntity+).*(..))")
    public void excludedEntity() {
    }*/
    @Pointcut("execution(* com.cola..entity..*.*(..))")
        public void excludedEntity(){
    }

    @Around("includedPackages() && !excluedTests() && !excludedPackages() && !excludedConfiguration() && !excludedApplicationRunner() && !excludedEntity()")
    public Object aspectLoggingStatistics(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();

        Method proxyMethod = ((MethodSignature) signature).getMethod();
        Parameter[] parameters = proxyMethod.getParameters();
        Class<?> returnType = proxyMethod.getReturnType();

        StringBuilder builder = new StringBuilder();
        builder.append(returnType.getName()).append(" ").append(proxyMethod.getName()).append("(");
        int i = 0;
        for (Parameter parameter : parameters) {
            i++;
            builder.append(parameter.getType().getName()).append(" ").append(parameter.getName()).append(",");
        }
        if (i > 0) builder.delete(builder.length() - 1, builder.length());
        builder.append(")");

        Class declaringType = signature.getDeclaringType();
        logger = LoggerFactory.getLogger(declaringType);
        long startTime = System.currentTimeMillis();
        Object proceed = pjp.proceed();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        String calledMethod = null;
        String calledClass = null;
        Thread thread = Thread.currentThread();
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 3) {
            for (i = 3; i < stackTraceElements.length; i++) {
                StackTraceElement stackTraceElement = stackTraceElements[i];
                if(stackTraceElement.getClassName().startsWith(DEFAULT_PACKAGE)){
                    calledClass = stackTraceElement.getClassName();
                    calledMethod = stackTraceElement.getMethodName();
                    break;
                }
            }
        }
        logger.debug("{threadName:\"" + thread.getName() + "\", methodName:\"" + builder.toString() + "\", totalTime:\"" + time + "ms\", calledClass:\"" + calledClass + "\", calledMethod:\"" + calledMethod + "\"}");
        return proceed;
    }
}
