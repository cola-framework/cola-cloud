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

import com.cola.libs.jpa.service.FlexibleSearchService;
import com.cola.libs.jpa.service.ModelService;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

/*
    @Test
    @Transactional(readOnly = true)
    public void entityGraphTest(){
        Map<String,Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", modelService.getEntityGraph("role.rolelps"));
        Role load = modelService.load(Role.class, 9L, properties);
        List<Rolelp> rolelps = load.getRolelps();

        properties.put("javax.persistence.fetchgraph", modelService.getEntityGraph("rolelp.all"));
        Rolelp load1 = modelService.load(Rolelp.class, 6L, properties);
        Role role = load1.getRole();
        Language language = load1.getLanguage();


        properties.put("javax.persistence.fetchgraph", modelService.getEntityGraph("role.rolelps"));
        Iterable query = flexibleSearchService.query(Role.class, (Specification) null, null, properties);
        Iterator iterator = query.iterator();
        while(iterator.hasNext()){
            Role next = (Role)iterator.next();
            List<Rolelp> rolelps1 = next.getRolelps();
        }

        Map<String,Object> condition = new HashMap<>();
        condition.put("role", load);
        //properties.put("javax.persistence.fetchgraph", modelService.getEntityGraph("rolelp.role"));
        Iterable query1 = flexibleSearchService.query(Rolelp.class, condition, null, null);

        String jpql = "select r from Rolelp r left join r.language l";
        Iterable<Rolelp> query2 = flexibleSearchService.query(jpql, Rolelp.class);

        String sql = "select r.code, rl.name from t_role_lp rl left join t_role r on (rl.role_id=r.id)";
        Iterable<List> lists = flexibleSearchService.nativeQuery(sql, List.class);

    }

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
    @Transactional(readOnly = true)
    public void complexTest(){
        String jpql = "select max(name) from Rolelp lp where lp.role=?";
        FlexibleQueryBuilder builder = new FlexibleQueryBuilder(jpql);
        long count = flexibleSearchService.count(Rolelp.class);

        Map<String, Object> condition = new HashMap<>();
        condition.put("code", "d207ba8f-d4f6-4d84-9");

        Role role = flexibleSearchService.uniqueQuery(Role.class, condition, null);

        if(role != null){
            Collection<Rolelp> rolelps = role.getRolelps();

            condition = new HashMap<>();
            condition.put("role", role);
            count  = flexibleSearchService.count(Rolelp.class, condition);

            builder.addParameter(role);
            String s = flexibleSearchService.uniqueQuery(builder, String.class);

            Pageable pageable = new PageRequest(0, 10, null);
            Page<String> strings = flexibleSearchService.pagingQuery(builder, String.class, pageable);
        }

        condition = new HashMap<>();
        condition.put("name", "ABC");
        Iterable<Rolelp> query = flexibleSearchService.query(Rolelp.class, condition, null, null);

        jpql = "select max(code) from Role";
        Object o = flexibleSearchService.uniqueQuery(jpql, null);

        jpql = "select lp from Rolelp lp left join lp.role r left join lp.language l";
        Iterable<Rolelp> query2 = flexibleSearchService.query(jpql, Rolelp.class);

        jpql = "select lp.name as name, r.code as code, l.isoCode as isoCode from Rolelp lp left join lp.role r left join lp.language l";
        Iterable<Map> query1 = flexibleSearchService.query(jpql, Map.class);

        Iterable<List> query3 = flexibleSearchService.query(jpql, List.class);

        Iterable<TestBean> query4 = flexibleSearchService.query(jpql, TestBean.class);
    }*/

}
