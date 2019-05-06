package net.arksea.config;

import java.io.Serializable;

/**
 *
 * Created by xiaohaixing on 2019/2/22.
 */
public class Login implements Serializable {
    public final String name;

    public Login(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public final String password;

}
