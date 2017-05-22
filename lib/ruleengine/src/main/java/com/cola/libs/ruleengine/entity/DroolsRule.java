package com.cola.libs.ruleengine.entity;

import com.cola.libs.jpa.entity.AbstractEntity;

import javax.persistence.*;

/**
 * Created by jiachen.shi on 11/9/2016.
 */
@Entity
@Cacheable
@Table(name = "t_drools_rule", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type", "code"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("droolsrule")
public class DroolsRule extends AbstractEntity {

    @Column(length = 20, nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean active;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String ruleContent;

    @Column(name = "checksum", length = 20, nullable = false)
    private String checkSum;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRuleContent() {
        return ruleContent;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
