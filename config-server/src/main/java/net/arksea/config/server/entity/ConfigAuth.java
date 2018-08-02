package net.arksea.config.server.entity;

import javax.persistence.*;

/**
 * 配置授权表
 * Created by xiaohaixing on 2018/7/31.
 */
@Entity
@Table(name = "cs_config_auth",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","config_id"}))
public class ConfigAuth extends IdEntity {
    private User user;
    private Config config;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="config_id",nullable = false)
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
