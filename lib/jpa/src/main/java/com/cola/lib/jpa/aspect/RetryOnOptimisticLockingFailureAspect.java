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
package com.cola.lib.jpa.aspect;

import com.cola.lib.jpa.annotation.RetryOnOptimisticLockingFailure;

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

import javax.persistence.OptimisticLockException;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
@Aspect
@DeclarePrecedence("com.cola.libs.jpa.aspect.RetryOnOptimisticLockingFailureAspect,org.springframework.transaction.aspectj.AnnotationTransactionAspect")
public class RetryOnOptimisticLockingFailureAspect {

    private static Logger logger;

    /**
     * Retry on opt failure.
     */
    @Pointcut("execution(@com.cola.libs.jpa.annotation.RetryOnOptimisticLockingFailure * *(..))")
    public void executionOfROLFMethod() {
    }

    @Pointcut("(execution(public * ((@com.cola.libs.jpa.annotation.RetryOnOptimisticLockingFailure *)+).*(..)) && @within(com.cola.libs.jpa.annotation.RetryOnOptimisticLockingFailure))")
    public void executionOfAnyPublicMethodInAtROLFType() {
    }

    /**
     * Do concurrent operation object.
     * @param pjp the pjp
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("executionOfROLFMethod() || executionOfAnyPublicMethodInAtROLFType()")
    public Object aspectROLFMethod(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        logger = LoggerFactory.getLogger(signature.getDeclaringType());
        Method proxyMethod = ((MethodSignature) signature).getMethod();
        Method soruceMethod = pjp.getTarget().getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
        RetryOnOptimisticLockingFailure annotation = soruceMethod.getAnnotation(RetryOnOptimisticLockingFailure.class);
        int maxRetries = annotation.maxRetries();
        int numAttempts = 0;
        do {
            numAttempts++;
            try {
                return pjp.proceed();
            } catch (OptimisticLockException ex) {
                if (numAttempts > maxRetries) {
                    throw ex;
                }
            }
            logger.debug("Retry on Optimistic Locking Failure. method:" + proxyMethod.getName() + " retry count:" + numAttempts);
        } while (numAttempts <= maxRetries);
        return null;
    }
}
