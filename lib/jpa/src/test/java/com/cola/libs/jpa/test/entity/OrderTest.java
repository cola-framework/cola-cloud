package com.cola.libs.jpa.test.entity;

import com.cola.libs.jpa.entity.AbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jiachen.shi on 11/25/2016.
 */
@Entity
@Cacheable
@Table(name = "t_order_test", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type", "code"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("order")
@NamedEntityGraphs(value = {@NamedEntityGraph(name = "order.orderItems", attributeNodes = @NamedAttributeNode("orderItems"))})
public class OrderTest extends AbstractEntity {

    @Column(length = 20, nullable = false)
    private String code;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy ="order")
    private List<OrderItemTest> orderItems;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<OrderItemTest> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemTest> orderItems) {
        this.orderItems = orderItems;
    }

}
