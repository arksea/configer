package net.arksea.config.server.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 *
 * Created by xiaohaixing on 2017/7/15.
 */
@Entity
@Table(name = "cs_config",uniqueConstraints = @UniqueConstraint(columnNames = {"project_id","name"}))
public class Config extends IdEntity {
    private Project project;
    private String name;
    private String description;
    private ConfigDoc doc;
    private int cacheMinutes;
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name="project_id",nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(length = 128, nullable = false)
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doc_id", unique = true, nullable = false)
    public ConfigDoc getDoc() {
        return doc;
    }

    public void setDoc(ConfigDoc doc) {
        this.doc = doc;
    }

    @Column(length = 256, nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(2) DEFAULT 0")
    public int getCacheMinutes() {
        return cacheMinutes;
    }

    public void setCacheMinutes(int cacheMinutes) {
        this.cacheMinutes = cacheMinutes;
    }

    @Override
    public String toString() {
        return project.getName()+":"+project.getProfile()+":"+name;
    }
}
