package net.arksea.config.server.entity;

import javax.persistence.*;

/**
 * 项目授权表
 * Created by xiaohaixing on 2018/8/1.
 */
@Entity
@Table(name = "cs_project_auth",uniqueConstraints = @UniqueConstraint(columnNames = {"project_id","user_id"}))
public class ProjectAuth extends IdEntity {
    private Project project;
    private User user;
    private boolean query;
    private boolean manage;
    private boolean config;

    @ManyToOne
    @JoinColumn(name="project_id",nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    public boolean isQuery() {
        return query;
    }

    public void setQuery(boolean query) {
        this.query = query;
    }

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
    }

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    public boolean isConfig() {
        return config;
    }

    public void setConfig(boolean config) {
        this.config = config;
    }
}
