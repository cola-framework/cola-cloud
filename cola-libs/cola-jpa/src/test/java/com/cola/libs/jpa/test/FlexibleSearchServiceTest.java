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

import com.cola.libs.jpa.entities.Language;
import com.cola.libs.jpa.entities.Role;
import com.cola.libs.jpa.services.FlexibleSearchService;
import com.cola.libs.jpa.services.ModelService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class FlexibleSearchServiceTest {

    private static Logger logger = LoggerFactory.getLogger(FlexibleSearchServiceTest.class);

    @Autowired
    private ModelService modelService;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Test
    @Transactional
    public void countTest(){
        UUID uuid = UUID.randomUUID();
        String isoCode = uuid.toString().substring(0, 20);
        Language language = new Language();
        language.setIsoCode(isoCode);
        language.setCreateBy(1L);
        language.setLastModifiedBy(1L);
        language = modelService.save(language);

        long count = flexibleSearchService.count(Language.class);
        Assert.assertEquals(count, 1L);

        Map<String, Serializable> condition = new  HashMap<>();
        condition.put("isoCode", isoCode);
        count = flexibleSearchService.count(Language.class, condition);
        Assert.assertEquals(count, 1L);

        condition.put("createBy", 2L);
        count = flexibleSearchService.count(Language.class, condition);
        Assert.assertEquals(count, 0L);
    }

    @Test
    @Transactional
    public void findAllTest(){

    }

}
