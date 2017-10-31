package com.cola.lib.jpa.test.entity;

import com.cola.lib.jpa.entity.AbstractEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jiachen.shi on 11/25/2016.
 */
@Entity
@Cacheable
@Table(name = "t_price_rows_test", indexes = {@Index(name="index_table_type", columnList = "table_type")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("price_rows")
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "priceRow.product", attributeNodes = @NamedAttributeNode(value = "product"))})
public class PriceRowTest extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.REFRESH}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", nullable = false)
    private ProductTest product;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    public ProductTest getProduct() {
        return product;
    }

    public void setProduct(ProductTest product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
