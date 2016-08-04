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

import com.cola.libs.jpa.entities.Order;
import com.cola.libs.jpa.entities.OrderItem;
import com.cola.libs.jpa.entities.Product;
import com.cola.libs.jpa.entities.Role;
import com.cola.libs.jpa.entities.Rolelp;
import com.cola.libs.jpa.services.ModelService;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.OutputCapture;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;

/**
 * cola
 * Created by jiachen.shi on 7/18/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ModelServiceTest {

    @Autowired
    private ModelService modelService;

    @Autowired
    private OptimisticLockingFailueTest optimisticLockingFailueTest;

    @Autowired
    private RetryOnOptimisticLockingFailureTest retryOnOptimisticLockingFailureTest;

    @Autowired
    private LazyLoadingTest lazyLoadingTest;

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    @Transactional
    public void cascadeTest(){

        Role role = lazyLoadingTest.init();
        Role newRole = lazyLoadingTest.test(role.getId());
        lazyLoadingTest.destroy(role);

        UUID uuid = UUID.randomUUID();
        Product product = new Product();
        product.setCode(uuid.toString().substring(0, 20));
        product.setLastModifiedBy(1L);
        product.setCreateBy(1L);
        product = modelService.save(product);

        uuid = UUID.randomUUID();
        Order order = new Order();
        order.setCreateBy(1L);
        order.setLastModifiedBy(1L);
        order.setCode(uuid.toString().substring(0, 20));

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        orderItem.setLastModifiedBy(1L);
        orderItem.setCreateBy(1L);
        orderItem.setProduct(product);
        orderItem.setPrice(new BigDecimal(10.00));
        orderItem.setOrder(order);
        orderItem.setCreateTime(new Date());
        orderItem.setLastModifiedTime(new Date());
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);
        order = modelService.save(order);

        modelService.delete(order);
    }

    @Test
    @Transactional
    public void singleEntityTest(){

        Role role = new Role();
        role.setCode("MTIzNDU2Nzg5");
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);

        //Via Save method to create entity
        Role newRole = modelService.save(role);
        Role loadRole = modelService.load(Role.class, newRole.getId());
        Assert.assertEquals(newRole.getCode(), loadRole.getCode());

        //Via Save method to update entity
        loadRole.setCode("OTg3NjU0MzIx");
        modelService.save(loadRole);
        Role updatedRole = modelService.load(Role.class, loadRole.getId());
        Assert.assertEquals(updatedRole.getCode(), loadRole.getCode());

        //For get method test
        Role getRole = modelService.get(Role.class, loadRole.getId());
        Assert.assertEquals(updatedRole.getCode(), getRole.getCode());

        //For load method with LoadModeType test
        modelService.load(Role.class, loadRole.getId(), LockModeType.PESSIMISTIC_WRITE);
        Assert.assertThat(capture.toString(), Matchers.containsString("for update"));

        //Test entity is exist
        boolean exists = modelService.exists(Role.class, updatedRole.getId());
        Assert.assertTrue(exists);

        //For delete(Class<T> tClass, ID id) method test
        modelService.delete(Role.class, newRole.getId());
        Role deletedRole = modelService.load(Role.class, newRole.getId());
        Assert.assertNull(deletedRole);

        //Test entity is not exist
        exists = modelService.exists(Role.class, updatedRole.getId());
        Assert.assertFalse(exists);

        //For delete(T entity method test
        Role renewRole = modelService.save(role);
        exists = modelService.exists(Role.class, renewRole.getId());
        Assert.assertTrue(exists);
        modelService.delete(renewRole);
        exists = modelService.exists(Role.class, renewRole.getId());
        Assert.assertFalse(exists);
    }

    @Test
    public void optimisticLockingFailureTest(){
        try {
            optimisticLockingFailueTest.test();
        }catch (Exception e){
            System.out.println("catchOptimisticLockingFailure");
            Assert.assertTrue(e instanceof OptimisticLockException);
        }
        Assert.assertThat(capture.toString(), Matchers.containsString("catchOptimisticLockingFailure"));
    }

    @Test
    public void retryOnOptimisticLockingFailureTest(){
        try{
            retryOnOptimisticLockingFailureTest.test();
        }catch (Exception e){
            Assert.assertTrue(e instanceof OptimisticLockException);
        }
        Assert.assertThat(capture.toString(), Matchers.containsString("retryNum:3"));
    }

    @Test
    public void versionIncrementTest(){

        Role role = new Role();
        role.setCode("MTIzNDU2Nzg5");
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);

        //Test version column is valid
        Role newRole = modelService.save(role);
        Role loadRole = modelService.load(Role.class, newRole.getId());
        Assert.assertEquals(loadRole.getVersion().longValue(), 0L);

        loadRole.setCode("OTg3NjU0MzIx");
        modelService.save(loadRole);
        Role updatedRole = modelService.load(Role.class, loadRole.getId());
        Assert.assertEquals(updatedRole.getVersion().longValue(), 1L);

        updatedRole.setCode("MTIxMjEyMTIx");
        modelService.save(updatedRole);
        updatedRole = modelService.load(Role.class, updatedRole.getId());
        Assert.assertEquals(updatedRole.getVersion().longValue(), 2L);

        //Test execute method
        String jpql = "update Role set code=:code where id=:id";
        Map<String, Object> params = new HashMap<>();
        params.put("code", "KjMyMzIzMjMy");
        params.put("id", updatedRole.getId());
        int result = modelService.execute(jpql, params);
        Assert.assertEquals(result, 1);

        updatedRole = modelService.load(Role.class, updatedRole.getId());
        Assert.assertEquals(updatedRole.getVersion().longValue(), 3L);

        modelService.delete(updatedRole);
    }

    @Test
    public void multiEntityTest(){

        List<Role> roleList = new ArrayList<Role>();

        Role role = new Role();
        role.setCode("MTIzNDU2Nzg5");
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);
        roleList.add(role);

        role = new Role();
        role.setCode("NDc4MzkyMDM5");
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);
        roleList.add(role);

        role = new Role();
        role.setCode("UjkzODQ4NTc4");
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);
        roleList.add(role);

        List<Role> newRoleList = modelService.save(roleList);
        Assert.assertNotNull(newRoleList);
        Assert.assertEquals(newRoleList.size(), 3);

        modelService.deleteInBatch(newRoleList);

        newRoleList = modelService.save(roleList);
        modelService.delete(newRoleList);

        newRoleList = modelService.save(roleList);
        modelService.deleteAll(Role.class);

    }


}
