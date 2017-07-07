package com.cola.libs.jpa.test.entity;

import com.cola.libs.jpa.entity.AbstractEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jiachen.shi on 11/25/2016.
 */
@Entity
@Cacheable
@Table(name = "t_order_items_test", indexes = {@Index(name = "index_order", columnList = "table_type, order_id")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("order_items")
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "orderItem.order", attributeNodes = @NamedAttributeNode(value = "order"))})
public class OrderItemTest extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable = false)
    private OrderTest order;

    @ManyToOne(cascade = {}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", nullable = false)
    private ProductTest product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public OrderTest getOrder() {
        return order;
    }

    public void setOrder(OrderTest order) {
        this.order = order;
    }

    public ProductTest getProduct() {
        return product;
    }

    public void setProduct(ProductTest product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
