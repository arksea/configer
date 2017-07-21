package net.arksea.config;

import net.arksea.acache.CacheAskException;

/**
 * Created by xiaohaixing on 2017/7/21.
 */
public class FloatConfig extends AbstractConfig<Float> {
    public FloatConfig(String key, Float defaultValue, ConfigService service) {
        super(key, defaultValue, service);
    }
    public FloatConfig(String key, ConfigService service) throws CacheAskException {
        super(key, service);
    }
    @Override
    protected Float parseValue(String str) {
        return Float.valueOf(str);
    }
}
