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
package com.cola.libs.jpa.test;

import com.cola.libs.jpa.annotations.RetryOnOptimisticLockingFailure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * cola
 * Created by jiachen.shi on 7/22/2016.
 */
@Component
public class RetryOnOptimisticLockingFailureTest {

    @Autowired
    private OptimisticLockingFailueTest optimisticLockingFailueTest;

    private int retryNum = -1;

    @RetryOnOptimisticLockingFailure
    public void test(){
        this.retryNum++;
        System.out.println("retryNum:"+retryNum);
        optimisticLockingFailueTest.test();
    }

}
