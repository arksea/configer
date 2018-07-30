package net.arksea.config.server.login;

/**
 *
 * Created by xiaohaixing on 2017/11/1.
 */
public enum LoginStatus {
    SUCCEED(0), FAILED(1), INVALID(2);
    private int value;
    LoginStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
