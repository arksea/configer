package net.arksea.config;

import net.arksea.acache.CacheAskException;

/**
 * Created by xiaohaixing on 2017/7/21.
 */
public class IntConfig extends AbstractConfig<Integer> {
    public IntConfig(String key, ConfigService service) throws CacheAskException {
        super(key, service);
    }
    @Override
    protected Integer parseValue(String str) {
        return Integer.valueOf(str);
    }
}
