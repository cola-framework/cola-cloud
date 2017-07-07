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

import com.cola.libs.jpa.entity.OrderItem;
import com.cola.libs.jpa.service.FlexibleSearchService;
import com.cola.libs.jpa.service.ModelService;
import com.cola.libs.jpa.test.entity.OrderItemTest;
import com.cola.libs.jpa.test.entity.OrderTest;
import com.cola.libs.jpa.test.entity.PriceRowTest;
import com.cola.libs.jpa.test.entity.ProductTest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * cola
 * Created by jiachen.shi on 7/18/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Transactional
public class ModelServiceTest {

    private static Logger logger = LoggerFactory.getLogger(ModelServiceTest.class);

    @Autowired
    private ModelService modelService;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Autowired
    private OptimisticLockingFailueTest optimisticLockingFailueTest;

    @Autowired
    private LazyLoadingTest lazyLoadingTest;

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    public void commonTest(){

        PriceRowTest priceRow = new PriceRowTest();
        priceRow.setPrice(new BigDecimal(10.00));
        priceRow.setStartTime(new Date());
        priceRow.setEndTime(new Date());
        priceRow.setCreateBy(1L);
        priceRow.setLastModifiedBy(1L);

        List<PriceRowTest> priceRows = new ArrayList<>();
        priceRows.add(priceRow);

        ProductTest product1 = new ProductTest();
        product1.setCode("001");
        product1.setPriceRows(priceRows);
        product1.setCreateBy(1L);
        product1.setLastModifiedBy(1L);
        priceRow.setProduct(product1);

        priceRow = new PriceRowTest();
        priceRow.setPrice(new BigDecimal(102.00));
        priceRow.setStartTime(new Date());
        priceRow.setEndTime(new Date());
        priceRow.setCreateBy(1L);
        priceRow.setLastModifiedBy(1L);

        priceRows = new ArrayList<>();
        priceRows.add(priceRow);

        ProductTest product2 = new ProductTest();
        product2.setCode("002");
        product2.setPriceRows(priceRows);
        product2.setCreateBy(1L);
        product2.setLastModifiedBy(1L);
        priceRow.setProduct(product2);

        List<ProductTest> productTests = new ArrayList<>();
        productTests.add(product1);
        productTests.add(product2);

        List<ProductTest> savedProducts = modelService.save(productTests);
        ProductTest savedProduct = savedProducts.get(0);
        ProductTest loadProduct = modelService.load(ProductTest.class, savedProduct.getId());
        Assert.assertEquals(savedProduct.getCode(), loadProduct.getCode());

        OrderItemTest orderItem1 = new OrderItemTest();
        orderItem1.setPrice(new BigDecimal(9.00));
        orderItem1.setProduct(savedProduct);
        orderItem1.setQuantity(1);
        orderItem1.setCreateBy(1L);
        orderItem1.setLastModifiedBy(1L);

        ProductTest savedProduct2 = savedProducts.get(1);
        OrderItemTest orderItem2 = new OrderItemTest();
        orderItem2.setPrice(new BigDecimal(8.00));
        orderItem2.setProduct(savedProduct2);
        orderItem2.setQuantity(2);
        orderItem2.setCreateBy(1L);
        orderItem2.setLastModifiedBy(1L);

        List<OrderItemTest> orderItems = new ArrayList<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        OrderTest order = new OrderTest();
        order.setCode("001");
        order.setOrderItems(orderItems);
        order.setCreateBy(1L);
        order.setLastModifiedBy(1L);
        orderItem1.setOrder(order);
        orderItem2.setOrder(order);

        OrderTest savedOrder = modelService.save(order);



    }


/*    @Test
    @Transactional
    public void L2CacheTest(){

        UUID uuid = UUID.randomUUID();
        String code = uuid.toString().substring(0, 20);

        Role role = new Role();
        role.setCode(code);
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);
        Role newRole = modelService.save(role);

        Role load = modelService.load(Role.class, 12L);

        List<Rolelp> rolelps = load.getRolelps();
        int size = rolelps.size();

        uuid = UUID.randomUUID();
        code = uuid.toString().substring(0, 20);
        load.setCode(code);
        modelService.save(load);

        String jpql = "select r.code from Role r";
        Iterable<Role> query = flexibleSearchService.query(jpql, Role.class);

        jpql = "from Role";
        query = flexibleSearchService.query(jpql, Role.class);

        uuid = UUID.randomUUID();
        code = uuid.toString().substring(0, 19);
        role = new Role();
        role.setCode(code);
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);
        Role save = modelService.save(role);

        jpql = "from Role";
        query = flexibleSearchService.query(jpql, Role.class);

        jpql = "select r.code, rl.name from Role r, Rolelp rl where r.id=rl.role";
        Iterable<Map> query1 = flexibleSearchService.query(jpql, Map.class);

        jpql = "from Role where id=12";
        query = flexibleSearchService.query(jpql, Role.class);

        jpql = "delete from Role where id=?";
        List params = new ArrayList<>();
        params.add(25L);
        modelService.execute(jpql, params);

        uuid = UUID.randomUUID();
        code = uuid.toString().substring(0, 18);
        jpql = "update Role set code=? where id=?";
        params = new ArrayList<>();
        params.add(code);
        params.add(12L);
        modelService.execute(jpql, params);

        jpql = "from Role";
        query = flexibleSearchService.query(jpql, Role.class);

    }*/

/*    @Test
    @Transactional
    public void cacheTest(){
        Role role = new Role();
        role.setCode("MTIzNDU2Nzg5");
        role.setCreateBy(111L);
        role.setLastModifiedBy(111L);
        Role newRole = modelService.save(role);

        Role load = modelService.load(Role.class, 12L);

        List<Rolelp> rolelps2 = load.getRolelps();

        Role load1 = modelService.load(Role.class, newRole.getId());

        List<Rolelp> rolelps1 = load1.getRolelps();
        modelService.save(load1);

        UUID uuid = UUID.randomUUID();
        String code = uuid.toString().substring(0, 20);
        load.setCode(code);
        modelService.save(load);

        load = modelService.load(Role.class, 12L);

        load1 = modelService.load(Role.class, newRole.getId());

        List<Role> list = new ArrayList<>();
        list.add(load1);
        list = modelService.save(list);

        modelService.delete(list, true);

        modelService.deleteAll(Rolelp.class, true);
        modelService.deleteAll(Role.class, true);

        List<Rolelp> rolelps = load1.getRolelps();
    }*/


    @Test
    public void cascadeTest(){
        UUID uuid = UUID.randomUUID();
        ProductTest product = new ProductTest();
        product.setCode(uuid.toString().substring(0, 20));
        product.setLastModifiedBy(1L);
        product.setCreateBy(1L);
        product = modelService.save(product);

        uuid = UUID.randomUUID();
        OrderTest order = new OrderTest();
        order.setCreateBy(1L);
        order.setLastModifiedBy(1L);
        order.setCode(uuid.toString().substring(0, 20));
        modelService.save(order);

        List<OrderItemTest> orderItems = new ArrayList<>();
        OrderItemTest orderItem = new OrderItemTest();
        orderItem.setQuantity(1);
        orderItem.setLastModifiedBy(1L);
        orderItem.setCreateBy(1L);
        orderItem.setProduct(product);
        orderItem.setPrice(new BigDecimal(10.00));
        orderItem.setOrder(order);
        orderItem.setCreateTime(new Date());
        orderItem.setLastModifiedTime(new Date());
        orderItem.setDeleted(false);
        orderItems.add(orderItem);
        modelService.save(orderItems);

/*        order.setOrderItems(orderItems);
        modelService.save(order);*/

        uuid = UUID.randomUUID();
        //order.setCode(uuid.toString().substring(0, 20));
        orderItem.setPrice(new BigDecimal(20.00));
        modelService.save(orderItem);

        uuid = UUID.randomUUID();
        order.setCode(uuid.toString().substring(0, 20));
        //orderItem.setPrice(new BigDecimal(25.00));
        modelService.save(order);

        long count = flexibleSearchService.count(OrderTest.class);
        Assert.assertEquals(count, 1L);

        count = flexibleSearchService.count(OrderItemTest.class);
        Assert.assertEquals(count, 1L);

        count = flexibleSearchService.count(ProductTest.class);
        Assert.assertEquals(count, 1L);
    }


/*    @Test
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

        List<Rolelp> rolelps = loadRole.getRolelps();

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
        modelService.delete(Role.class, newRole.getId(), true);
        Role deletedRole = modelService.load(Role.class, newRole.getId());
        Assert.assertNull(deletedRole);

        //Test entity is not exist
        exists = modelService.exists(Role.class, updatedRole.getId());
        Assert.assertFalse(exists);

        //For delete(T entity method test
        Role renewRole = modelService.save(role);
        exists = modelService.exists(Role.class, renewRole.getId());
        Assert.assertTrue(exists);
        modelService.delete(renewRole, true);
        exists = modelService.exists(Role.class, renewRole.getId());
        Assert.assertFalse(exists);
    }*/

/*    @Test
    public void retryOnOptimisticLockingFailureTest(){
        try {
            optimisticLockingFailueTest.test();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("catchOptimisticLockingFailure");
            Assert.assertTrue(e instanceof OptimisticLockException);
        }
        Assert.assertThat(capture.toString(), Matchers.containsString("catchOptimisticLockingFailure"));
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
        String jpql = "update Role r set r.code=:code where r.id=:id";
        Map<String, Object> params = new HashMap<>();
        params.put("code", "KjMyMzIzMjMy");
        params.put("id", updatedRole.getId());
        int result = modelService.execute(jpql, params);
        Assert.assertEquals(result, 1);

        updatedRole = modelService.load(Role.class, updatedRole.getId());
        Assert.assertEquals(updatedRole.getVersion().longValue(), 3L);

        modelService.delete(updatedRole, true);
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

        modelService.deleteInBatch(newRoleList, true);

        newRoleList = modelService.save(roleList);
        modelService.delete(newRoleList, true);

        newRoleList = modelService.save(roleList);
        modelService.deleteAll(Role.class, true);

    }*/


}
