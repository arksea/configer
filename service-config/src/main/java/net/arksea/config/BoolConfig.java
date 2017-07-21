package net.arksea.config;

import net.arksea.acache.CacheAskException;

/**
 *
 * Created by xiaohaixing on 2017/7/21.
 */
public class BoolConfig extends AbstractConfig<Boolean> {
    public BoolConfig(String key, ConfigService service) throws CacheAskException {
        super(key, service);
    }
    @Override
    protected Boolean parseValue(String str) {
        return Boolean.valueOf(str);
    }
}
