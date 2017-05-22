package com.cola.libs.ruleengine.entity;

import com.cola.libs.jpa.entity.AbstractEntity;
import com.cola.libs.ruleengine.beans.SessionType;

import javax.persistence.*;

/**
 * Created by jiachen.shi on 11/3/2016.
 */
@Entity
@Cacheable
@Table(name = "t_drools_session", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type", "code"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("session")
public class DroolsKIESession extends AbstractEntity {

    @Column(length = 20, nullable = false)
    private String code;

    @Column(name = "session_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @Column(name = "rule_group", length = 20, nullable = false)
    private String ruleGroup;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="base", nullable = false)
    private DroolsKIEBase droolsKIEBase;

    public DroolsKIEBase getDroolsKIEBase() {
        return droolsKIEBase;
    }

    public void setDroolsKIEBase(DroolsKIEBase droolsKIEBase) {
        this.droolsKIEBase = droolsKIEBase;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(String ruleGroup) {
        this.ruleGroup = ruleGroup;
    }
}
