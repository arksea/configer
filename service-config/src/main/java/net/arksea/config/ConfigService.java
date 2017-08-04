package net.arksea.config;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.arksea.acache.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Created by xiaohaixing on 2017/7/12.
 */
public class ConfigService {
    private static Logger logger = LogManager.getLogger(ConfigService.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final long CACHE_DEFAULT_TIMEOUT = 300_000;
    private final ActorSystem system;
    private final int timeout;
    private final String project;
    private CacheAsker<String, String> localArticleCache;
    private IConfigPersistence configPersistence;
    private final Map<String,TimedData<Object>> configMap = new ConcurrentHashMap<>();
    private final String cacheKeyPre;
    private final String storeKeyPre;
    private final String profile;

    public ConfigService(final ActorSystem system,
                         final List<String> serverPaths,
                         final String project,
                         final String profile,
                         final int timeout) {
        this.system  = system;
        this.project = project;
        this.profile = profile;
        this.cacheKeyPre = project+":"+profile+":";
        this.storeKeyPre = profile+":";
        this.timeout = timeout;
        String filePath = "./config/" + project + ".cfg";
        configPersistence = new FilePersistence(filePath);
        createLocalCache(serverPaths);
    }

    public Map getMap(String key) {
        return get(key, Map.class);
    }

    public Integer getIngeter(String key) {
        return get(key, Integer.class);
    }

    public Long getLong(String key) {
        return get(key, Long.class);
    }

    public Double getDouble(String key) {
        return get(key, Double.class);
    }

    public Float getFloat(String key) {
        return get(key, Float.class);
    }

    public String getString(String key) {
        return get(key, String.class);
    }

    public Boolean getBoolean(String key) {
        return get(key, Boolean.class);
    }

    public List getList(String key) {
        return get(key, List.class);
    }

    private <T> T get(String key, Class<T> clazz) {
        TimedData td = configMap.get(key);
        if (td == null) {
            String cacheKey = cacheKeyPre + key;
            String storeKey = storeKeyPre + key;
            GetData<String,String> req = new GetData<>(cacheKey);
            try { //配置初始值优先从配置服务缓存读取
                DataResult<String,String> ret = Await.result(localArticleCache.ask(req), Duration.create(timeout, "ms"));
                if (ret.data != null) {
                    T obj = objectMapper.readValue(ret.data, clazz);
                    configMap.put(key, new TimedData<>(ret.expiredTime, obj));
                    configPersistence.update(storeKey, ret.data); //从服务读取的配置可以正确解析则更新本地的持久化数据
                    return obj;
                }
            } catch (Exception e) {
                logger.warn("从服务取配置失败: {}", cacheKey, e);
            }
            //从本地持久化数据读取
            String value = configPersistence.read(storeKey);
            if (value == null) {
                throw new RuntimeException("初始化配置失败: " + cacheKey);
            } else {
                try {
                    T obj = objectMapper.readValue(value, clazz);
                    configMap.put(key, new TimedData<>(System.currentTimeMillis() + CACHE_DEFAULT_TIMEOUT, obj));
                    return obj;
                } catch (IOException ex) {
                    throw new RuntimeException("配置格式错误key="+cacheKey+",value="+value, ex);
                }
            }
        } else if (System.currentTimeMillis() > td.time) {
            asyncUpdate(key);
            return (T) td.data;
        } else {
            return (T) td.data;
        }
    }

    private void asyncUpdate(String key) {
        String cacheKey = cacheKeyPre +key;
        String storeKey = storeKeyPre + key;
        GetData<String,String> req = new GetData<>(cacheKey);
        localArticleCache.ask(req).onComplete(
            new OnComplete<DataResult<String,String>> () {
                @Override
                public void onComplete(Throwable failure, DataResult<String, String> success) throws Throwable {
                    if (failure == null) {
                        try {
                            Map map = objectMapper.readValue(success.data, Map.class);
                            configMap.put(key, new TimedData<>(System.currentTimeMillis() + CACHE_DEFAULT_TIMEOUT, map));
                            configPersistence.update(storeKey, success.data);
                        } catch (IOException ex) {
                            logger.warn("配置格式错误key={},value={}", key, success.data, ex);
                        }
                    } else {
                        logger.warn("更新配置失败: ", key , failure);
                    }
                }
            }, system.dispatcher()
        );
    }

    private void createLocalCache(final List<String> serverPaths) {
        ICacheConfig<String> cfg = new ICacheConfig<String>() {
            @Override
            public String getCacheName() {
                return "localConfigCache";
            }
        };
        localArticleCache = LocalCacheCreator.createLocalCache(system, cfg, serverPaths, timeout, timeout);
    }
}
