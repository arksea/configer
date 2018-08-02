package net.arksea.config.server.entity;

import javax.persistence.*;

/**
 * 项目授权表
 * Created by xiaohaixing on 2018/8/1.
 */
@Entity
@Table(name = "cs_project_auth",uniqueConstraints = @UniqueConstraint(columnNames = {"project_id","user_id","function"}))
public class ProjectAuth extends IdEntity {
    private Project project;
    private User user;
    private ProjectFunction function;

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

    @Column(columnDefinition = "TINYINT(1) NOT NULL")
    @Enumerated(EnumType.ORDINAL)
    public ProjectFunction getFunction() {
        return function;
    }

    public void setFunction(ProjectFunction function) {
        this.function = function;
    }
}
