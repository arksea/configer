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

    @Override
    public String toString() {
        return project.getName()+":"+project.getProfile()+":"+name;
    }
}
