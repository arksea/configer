package net.arksea.config;

import net.arksea.acache.DataResult;
import org.apache.commons.lang3.StringUtils;
import scala.concurrent.Future;

/**
 *
 * Created by xiaohaixing on 2017/7/21.
 */
public abstract class AbstractConfig<T> {
    private final String key;
    private final ConfigService service;

    public AbstractConfig(String key, ConfigService service) {
        this.key = key;
        this.service = service;
        service.syncGet(key, 10000);
    }

    public T get() {
        Future<DataResult<String,String>> future = service.get(key);

        if (StringUtils.isEmpty(value)) {
            throw new RuntimeException("读配置失败："+key);
        }
        return parseValue(value);
    }

    public T get(T defValue) {
        String value = service.get(key);
        if (StringUtils.isEmpty(value)) {
            return defValue;
        } else {
            return parseValue(value);
        }
    }

    protected abstract T parseValue(String str);
}
