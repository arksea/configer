package net.arksea.config.server.entity;

import javax.persistence.*;
import java.util.Map;

/**
 *
 * Created by xiaohaixing on 2017/7/15.
 */
@Entity
@Table(name = "cs_project",uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Project extends IdEntity {
    private String name;
    private String description;
    private Map<String, Config> configs;

    @Column(length = 128, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 256, nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "project")
    @MapKey(name="name")
    public Map<String, Config> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, Config> configs) {
        this.configs = configs;
    }
}
