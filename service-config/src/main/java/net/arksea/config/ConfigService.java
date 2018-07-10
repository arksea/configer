package net.arksea.config;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.arksea.acache.*;
import net.arksea.acache.dsf.CacheDsfAsker;
import net.arksea.dsf.client.Client;
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
    private static final long RETRY_DELAY = 30_000;
    public  final ActorSystem system;
    public  final String project;
    public  final String profile;
    public  final int timeout;
    private CacheAsker<ConfigKey, String> localArticleCache;
    private IConfigPersistence configPersistence;
    private final Map<String,TimedData<Object>> configMap = new ConcurrentHashMap<>();

    public ConfigService(final Client dsfClient, final String project, final String profile) {
        this(dsfClient, project, profile, 5000, dsfClient.system);
    }

    public ConfigService(final Client dsfClient,
                         final String project,
                         final String profile,
                         final int timeout) {
        this(dsfClient, project, profile, timeout, dsfClient.system);
    }

    public ConfigService(final Client dsfClient,
                         final String project,
                         final String profile,
                         final int timeout,
                         final ActorSystem system) {
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
        CacheDsfAsker<ConfigKey,String> remoteCacheAsker = new CacheDsfAsker<>(dsfClient,this.system.dispatcher(),timeout);
        createLocalCache(remoteCacheAsker);
    }


    public ConfigService(final String serverAddr, final String project,final String profile) {
        this(serverAddr, project, profile, 5000, null);
    }

    public ConfigService(final String serverAddr,
                         final String project,
                         final String profile,
                         final int timeout) {
        this(serverAddr, project, profile, timeout, null);
    }

    public ConfigService(final String serverAddr,
                         final String project,
                         final String profile,
                         final int timeout,
                         final ActorSystem system) {
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
        ActorSelection remoteCacheSel = this.system.actorSelection("akka.tcp://system@"+serverAddr+"/user/configCacheServer");
        CacheAsker<ConfigKey,String> remoteCacheAsker = new CacheAsker<>(remoteCacheSel,this.system.dispatcher(),timeout);
        createLocalCache(remoteCacheAsker);
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

    public <T> T get(String key, Class<T> clazz, T defaultValue) {
        try {
            return get(key, clazz);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(),ex);
            return defaultValue;
        }
    }

    private <T> T get(String key, Class<T> clazz) {
        TimedData td = configMap.get(key);
        if (td == null) {
            ConfigKey configKey = new ConfigKey(project, profile, key);
            logger.info("从服务读取配置: {}", configKey);
            GetData<ConfigKey,String> req = new GetData<>(configKey);
            try { //配置初始值优先从配置服务读取，防止应用启动后首次读取本地配置造成与配置服务的值不同
                DataResult<ConfigKey,String> ret = Await.result(localArticleCache.ask(req), Duration.create(timeout, "ms"));
                if (ret.data != null) {
                    T obj = objectMapper.readValue(ret.data, clazz);
                    configMap.put(key, new TimedData<>(ret.expiredTime, obj));
                    configPersistence.update(key, ret.data); //从服务读取的配置可以正确解析则更新本地的持久化数据
                    return obj;
                } else {
                    logger.warn("访问配置服务失败，没找到相关内容: "+configKey);
                }
            } catch (Exception e) {
                logger.warn("访问配置服务失败，将尝试从本地文件读取: "+configKey, e);
            }
            //从本地持久化数据读取
            String value = configPersistence.read(key);
            if (value != null) {
                try {
                    T obj = objectMapper.readValue(value, clazz);
                    configMap.put(key, new TimedData<>(System.currentTimeMillis() + RETRY_DELAY, obj));
                    return obj;
                } catch (IOException ex) {
                    logger.error("配置文件格式错误，key={},value={}", configKey, value, ex);
                    throw new RuntimeException("取配置信息失败：+"+configKey);
                }
            } else {
                throw new RuntimeException("取配置信息失败："+configKey);
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
                            T value = (T) objectMapper.readValue(success.data, old.data.getClass());
                            long expiredTime = success.expiredTime;
                            //防止意外的频繁访问服务
                            if (expiredTime < System.currentTimeMillis() + RETRY_DELAY) {
                                expiredTime += RETRY_DELAY;
                            }
                            configMap.put(key, new TimedData<>(expiredTime, value));
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

    private void createLocalCache(ICacheAsker<ConfigKey, String> remoteCacheAsker) {
        ICacheConfig<ConfigKey> cfg = new ICacheConfig<ConfigKey>() {
            @Override
            public String getCacheName() {
                return "localConfigCache";
            }
        };
        localArticleCache = LocalCacheCreator.createLocalCache(system, cfg, remoteCacheAsker, timeout, timeout * 5);
    }
}
