package com.cola.libs.ruleengine.entity;

import com.cola.libs.jpa.entity.AbstractEntity;
import com.cola.libs.ruleengine.beans.EqualityBehavior;
import com.cola.libs.ruleengine.beans.EventProcessingMode;

import javax.persistence.*;

/**
 * Created by jiachen.shi on 11/3/2016.
 */
@Entity
@Cacheable
@Table(name = "t_drools_base", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type", "code"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("base")
public class DroolsKIEBase extends AbstractEntity {

    @Column(length = 20, nullable = false)
    private String code;

    @Column(name = "equality_behavior", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EqualityBehavior equalityBehavior;

    @Column(name = "event_processing_mode", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventProcessingMode eventProcessingMode;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="module", nullable = false)
    private DroolsKIEModule droolsKIEModule;

    public DroolsKIEModule getDroolsKIEModule() {
        return droolsKIEModule;
    }

    public void setDroolsKIEModule(DroolsKIEModule droolsKIEModule) {
        this.droolsKIEModule = droolsKIEModule;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EqualityBehavior getEqualityBehavior() {
        return equalityBehavior;
    }

    public void setEqualityBehavior(EqualityBehavior equalityBehavior) {
        this.equalityBehavior = equalityBehavior;
    }

    public EventProcessingMode getEventProcessingMode() {
        return eventProcessingMode;
    }

    public void setEventProcessingMode(EventProcessingMode eventProcessingMode) {
        this.eventProcessingMode = eventProcessingMode;
    }
}
