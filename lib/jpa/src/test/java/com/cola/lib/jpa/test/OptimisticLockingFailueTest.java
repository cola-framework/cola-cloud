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
package com.cola.lib.jpa.test;

import com.cola.lib.jpa.service.ModelService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * cola
 * Created by jiachen.shi on 7/22/2016.
 */
public class OptimisticLockingFailueTest {

    @Autowired
    private ModelService modelService;

/*    @Transactional
    @RetryOnOptimisticLockingFailure
    public void test(){

        Role role = new Role();
        role.setCode("ODQ3NjM3NDU2");
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);

        Role newRole = modelService.save(role);

        String jpql = "update Role set code=:code where id=:id";
        Map<String, Object> params = new HashMap<>();
        params.put("code", "XjY0NzU4Njk3");
        params.put("id", newRole.getId());
        int result = modelService.execute(jpql, params);

        boolean exists = modelService.exists(Role.class, newRole.getId());
        Assert.assertTrue(exists);

        newRole.setCode("TDk4OTA5ODkw");
        modelService.save(newRole);

        exists = modelService.exists(Role.class, newRole.getId());
    }*/

}
