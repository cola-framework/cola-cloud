package com.cola.lib.jpa.test.entity;

import com.cola.lib.jpa.entity.AbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jiachen.shi on 11/25/2016.
 */
@Entity
@Cacheable
@Table(name = "t_product_test", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type","code"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("product")
@NamedEntityGraphs(value = {@NamedEntityGraph(name = "product.priceRows", attributeNodes = @NamedAttributeNode("priceRows"))})
public class ProductTest extends AbstractEntity {

    @Column(length = 20, nullable = false)
    private String code;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy ="product")
    private List<PriceRowTest> priceRows;

    /**
     * Gets code.
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    public List<PriceRowTest> getPriceRows() {
        return priceRows;
    }

    public void setPriceRows(List<PriceRowTest> priceRows) {
        this.priceRows = priceRows;
    }

}
