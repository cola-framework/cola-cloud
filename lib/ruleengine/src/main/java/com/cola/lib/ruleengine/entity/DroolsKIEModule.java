package com.cola.lib.ruleengine.entity;

import com.cola.lib.jpa.entity.AbstractEntity;

import javax.persistence.*;

/**
 * Created by jiachen.shi on 11/3/2016.
 */
@Entity
@Cacheable
@Table(name = "t_drools_module", uniqueConstraints = {@UniqueConstraint(columnNames = {"table_type", "mvn_groupid", "mvn_artifactid", "mvn_version"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("module")
public class DroolsKIEModule extends AbstractEntity {

    @Column(name = "mvn_groupid", length = 20, nullable = false)
    private String mvnGroupId;

    @Column(name = "mvn_artifactid", length = 20, nullable = false)
    private String mvnArtifactId;

    @Column(name = "mvn_version", length = 20, nullable = false)
    private String mvnVersion;

    public String getMvnGroupId() {
        return mvnGroupId;
    }

    public void setMvnGroupId(String mvnGroupId) {
        this.mvnGroupId = mvnGroupId;
    }

    public String getMvnArtifactId() {
        return mvnArtifactId;
    }

    public void setMvnArtifactId(String mvnArtifactId) {
        this.mvnArtifactId = mvnArtifactId;
    }

    public String getMvnVersion() {
        return mvnVersion;
    }

    public void setMvnVersion(String mvnVersion) {
        this.mvnVersion = mvnVersion;
    }
}
