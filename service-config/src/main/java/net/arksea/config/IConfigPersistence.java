package net.arksea.config;


/**
 *
 * Created by xiaohaixing on 2017/7/21.
 */
public interface IConfigPersistence {
    String read(String key);
    void update(String key, String value);
}
