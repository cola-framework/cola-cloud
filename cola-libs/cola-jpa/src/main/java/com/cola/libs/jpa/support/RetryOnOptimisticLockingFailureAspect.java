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
package com.cola.libs.jpa.support;

import com.cola.libs.jpa.annotations.RetryOnOptimisticLockingFailure;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.OptimisticLockingFailureException;

import java.lang.reflect.Method;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
@Aspect
public class RetryOnOptimisticLockingFailureAspect {

    /**
     * Retry on opt failure.
     */
    @Pointcut("@annotation(RetryOnOptimisticLockingFailure)")
    public void retryOnOptFailure() {
    }

    /**
     * Do concurrent operation object.
     * @param pjp the pjp
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("retryOnOptFailure()")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        Method method = joinPointObject.getMethod();
        RetryOnOptimisticLockingFailure annotation = method.getAnnotation(RetryOnOptimisticLockingFailure.class);
        int maxRetries = annotation.maxRetries();
        int numAttempts = 0;
        do {
            numAttempts++;
            try {
                return pjp.proceed();
            } catch (OptimisticLockingFailureException ex) {
                if (numAttempts > maxRetries) {
                    throw ex;
                }
            }
        } while (numAttempts <= maxRetries);
        return null;
    }
}
