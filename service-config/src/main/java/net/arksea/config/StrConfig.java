package net.arksea.config;

import net.arksea.acache.CacheAskException;

/**
 *
 * Created by xiaohaixing on 2017/7/21.
 */
public class StrConfig extends AbstractConfig<String> {
    public StrConfig(String key, ConfigService service) throws CacheAskException {
        super(key, service);
    }
    @Override
    protected String parseValue(String str) {
        return str;
    }
}
