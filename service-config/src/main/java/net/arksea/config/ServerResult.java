package net.arksea.config;

import java.io.Serializable;

/**
 *
 * Created by xiaohaixing on 2019/2/22.
 */
public class ServerResult implements Serializable {
    public final int code;
    public final String message;

    public ServerResult(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
