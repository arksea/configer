package net.arksea.config;

import net.arksea.config.ServerResult;

/**
 *
 * Created by xiaohaixing on 2019/2/22.
 */
public class LoginResult extends ServerResult {
    public final long expiredTime;
    public LoginResult(int code, String message, long expiredTime) {
        super(code, message);
        this.expiredTime = expiredTime;
    }

}
