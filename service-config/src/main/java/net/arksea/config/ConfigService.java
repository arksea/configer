package net.arksea.config;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.arksea.acache.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.LinkedList;
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
    private static final long RETRY_DELAY = 30_000;
    public  final ActorSystem system;
    public  final String project;
    public  final String profile;
    public  final int timeout;
    private CacheAsker<ConfigKey, String> localArticleCache;
    private IConfigPersistence configPersistence;
    private final Map<String,TimedData<Object>> configMap = new ConcurrentHashMap<>();

    public ConfigService(final String project,final String profile, final List<String> serverAddrs) {
        this(project, profile, 8000, null, serverAddrs);
    }

    public ConfigService(final String project,
                         final String profile,
                         final int timeout,
                         final ActorSystem system,
                         final List<String> serverAddrs) {
        if (system == null) {
            Config cfg = ConfigFactory.parseResources("default-system.conf");
            this.system = akka.actor.ActorSystem.create("configServiceSystem", cfg);
        } else {
            this.system = system;
        }
        this.project = project;
        this.profile = profile;
        this.timeout = timeout;
        String filePath = "./config/" + project + "." + profile + ".cfg";
        configPersistence = new FilePersistence(filePath);
        List<String> serverPaths = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        for (String addr : serverAddrs) {
            sb.append("akka.tcp://system@").append(addr).append("/user/configCacheServer");
            serverPaths.add(sb.toString());
            sb.setLength(0);
        }
        createLocalCache(serverPaths);
    }

    public Map getMap(String key) {
        return get(key, Map.class);
    }

    public Integer getInteger(String key) {
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
            ConfigKey configKey = new ConfigKey(project, profile, key);
            //从本地持久化数据读取
            String value = configPersistence.read(key);
            if (value != null) {
                try {
                    T obj = objectMapper.readValue(value, clazz);
                    configMap.put(key, new TimedData<>(System.currentTimeMillis() + RETRY_DELAY, obj));
                    return obj;
                } catch (IOException ex) {
                    logger.warn("本地保存的配置格式错误key={},value={}", configKey, value, ex);
                }
            }
            logger.warn("配置未持久化，将从服务读取: {}", configKey);
            GetData<ConfigKey,String> req = new GetData<>(configKey);
            try { //配置初始值优先从配置服务缓存读取
                DataResult<ConfigKey,String> ret = Await.result(localArticleCache.ask(req), Duration.create(timeout, "ms"));
                if (ret.data != null) {
                    T obj = objectMapper.readValue(ret.data, clazz);
                    configMap.put(key, new TimedData<>(ret.expiredTime, obj));
                    configPersistence.update(key, ret.data); //从服务读取的配置可以正确解析则更新本地的持久化数据
                    return obj;
                } else {
                    throw new RuntimeException("初始化配置失败, 服务没找到相关内容: "+configKey);
                }
            } catch (Exception e) {
                throw new RuntimeException("初始化配置失败: "+configKey, e);
            }
        } else if (System.currentTimeMillis() > td.time) {
            asyncUpdate(key,td);
            return (T) td.data;
        } else {
            return (T) td.data;
        }
    }

    private <T> void asyncUpdate(String key,TimedData<T> old) {
        ConfigKey configKey = new ConfigKey(project, profile, key);
        GetData<ConfigKey,String> req = new GetData<>(configKey);
        localArticleCache.ask(req).onComplete(
            new OnComplete<DataResult<ConfigKey,String>> () {
                @Override
                public void onComplete(Throwable failure, DataResult<ConfigKey, String> success) throws Throwable {
                    if (failure == null) {
                        try {
                            Map map = objectMapper.readValue(success.data, Map.class);
                            long expiredTime = success.expiredTime;
                            //防止意外的频繁访问服务
                            if (expiredTime < System.currentTimeMillis() + RETRY_DELAY) {
                                expiredTime += RETRY_DELAY;
                            }
                            configMap.put(key, new TimedData<>(expiredTime, map));
                            if (!old.data.equals(success.data)) {
                                configPersistence.update(key, success.data);
                            }
                        } catch (IOException ex) {
                            logger.warn("更新配置失败, 格式错误: key={},value={}", configKey, success.data, ex);
                        }
                    } else {
                        configMap.put(key, new TimedData<>(old.time+RETRY_DELAY, old.data));
                        logger.warn("更新配置失败: {}", configKey , failure);
                    }
                }
            }, system.dispatcher()
        );
    }

    private void createLocalCache(final List<String> serverPaths) {
        ICacheConfig<ConfigKey> cfg = new ICacheConfig<ConfigKey>() {
            @Override
            public String getCacheName() {
                return "localConfigCache";
            }
        };
        localArticleCache = LocalCacheCreator.createLocalCache(system, cfg, serverPaths, timeout, timeout * 5);
    }
}
