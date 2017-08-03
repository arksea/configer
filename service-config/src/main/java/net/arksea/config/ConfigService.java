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
    public final ActorSystem system;
    private final int timeout;
    private final String project;
    private CacheAsker<String, String> localArticleCache;
    private IConfigPersistence configPersistence;
    private final Map<String,TimedData<Object>> configMap = new ConcurrentHashMap<>();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final long CACHE_DEFAULT_TIMEOUT = 300_000;

    public ConfigService(final ActorSystem system, final List<String> serverPaths, final String project, final int timeout) {
        this.system = system;
        this.project = project;
        this.timeout = timeout;
        String filePath = "./config-persistence/" + project + ".cfg";
        configPersistence = new FilePersistence(filePath);
        createLocalCache(serverPaths);
    }

    private GetData<String,String> makeGetRequest(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(project).append(":").append(key);
        return new GetData<>(sb.toString());
    }

    public Map getAsMap(String key) {
        TimedData td = configMap.get(key);
        if (td == null) {
            String value = null;
            try { //配置初始值优先从配置服务缓存读取
                DataResult<String,String> ret = Await.result(localArticleCache.ask(makeGetRequest(key)), Duration.create(timeout, "ms"));
                if (ret.failed == null) {
                    if (ret.data != null) {
                        try {
                            Map map = objectMapper.readValue(ret.data, Map.class);
                            configMap.put(key, new TimedData<>(ret.expiredTime, map));
                            configPersistence.update(key, ret.data); //从服务读取的配置可以正确解析则更新本地的持久化数据
                            return map;
                        } catch (IOException ex) {
                            logger.warn("配置格式错误key={},value={}", key, ret.data, ex);
                        }
                    }
                } else {
                    logger.warn("从缓存取配置失败: {}", key, ret.failed);
                }
            } catch (Exception e) {
                logger.warn("从缓存取配置失败: {}", key, e);
            }
            value = configPersistence.read(key); //从本地持久化数据读取
            if (value == null) {
                throw new RuntimeException("初始化配置失败: " + key);
            } else {
                try {
                    Map map = objectMapper.readValue(value, Map.class);
                    configMap.put(key, new TimedData<>(System.currentTimeMillis() + CACHE_DEFAULT_TIMEOUT, map));
                    return map;
                } catch (IOException ex) {
                    throw new RuntimeException("配置格式错误key="+key+",value="+value, ex);
                }
            }
        } else if (System.currentTimeMillis() > td.time) {
            asyncUpdate(key);
            return (Map) td.data;
        } else {
            return (Map) td.data;
        }
    }

    private void asyncUpdate(String key) {
        localArticleCache.ask(makeGetRequest(key)).onComplete(
            new OnComplete<DataResult<String,String>> () {
                @Override
                public void onComplete(Throwable failure, DataResult<String, String> success) throws Throwable {
                    if (failure == null) {
                        if (success.failed == null) {
                            try {
                                Map map = objectMapper.readValue(success.data, Map.class);
                                configMap.put(key, new TimedData<>(System.currentTimeMillis() + CACHE_DEFAULT_TIMEOUT, map));
                                configPersistence.update(key, success.data);
                            } catch (IOException ex) {
                                logger.warn("配置格式错误key={},value={}", key, success.data, ex);
                            }
                        } else {
                            logger.warn("更新配置失败: ", key , success.failed);
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
