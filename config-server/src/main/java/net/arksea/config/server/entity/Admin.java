package net.arksea.config.server.entity;

import javax.persistence.*;

/**
 *
 * Created by xiaohaixing on 2018/8/1.
 */
@Entity
@Table(name = "cs_admin")
public class Admin extends IdEntity{
    private User user;

    @OneToOne
    @JoinColumn(name="user_id",nullable = false,unique = true)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
