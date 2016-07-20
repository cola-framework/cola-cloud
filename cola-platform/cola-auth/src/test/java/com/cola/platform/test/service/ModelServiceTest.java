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
package com.cola.platform.test.service;

import com.cola.libs.jpa.entities.Role;
import com.cola.libs.jpa.services.ModelService;
import com.cola.platform.auth.WebApplicationRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * cola
 * Created by jiachen.shi on 7/18/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebApplicationRunner.class)
@WebAppConfiguration
public class ModelServiceTest {

    private static Logger logger = LoggerFactory.getLogger(ModelServiceTest.class);

    @Autowired
    private ModelService modelService;

    public static void main(String[] args) {
        logger.info(Role.class.getName());
    }

    @Test
    public void saveTest(){

        Role role = new Role();
        role.setCode("111");
        role.setCreateBy(111L);
        role.setModifiBy(111L);

        modelService.save(role);
    }

}
