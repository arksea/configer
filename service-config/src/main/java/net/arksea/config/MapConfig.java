package net.arksea.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.arksea.acache.CacheAskException;

import java.io.IOException;
import java.util.Map;

/**
 *
 * Created by xiaohaixing on 2017/7/21.
 */
public class MapConfig extends AbstractConfig<Map> {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public MapConfig(String key, ConfigService service) throws CacheAskException {
        super(key, service);
    }
    @Override
    protected Map parseValue(String str) {
        try {
            return objectMapper.readValue(str, Map.class);
        } catch (IOException ex) {
            throw new RuntimeException("配置格式错误："+str, ex);
        }
    }
}
