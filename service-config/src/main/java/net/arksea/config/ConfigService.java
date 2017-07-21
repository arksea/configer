package net.arksea.config;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import net.arksea.acache.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;

/**
 * Created by xiaohaixing on 2017/7/12.
 */
public class ConfigService {
    private static Logger logger = LogManager.getLogger(ConfigService.class);
    public final ActorSystem system;
    private final int timeout;
    private final String project;
    private CacheAsker<String, String> localArticleCache;
    private IConfigPersistence configPersistence;

    public ConfigService(final ActorSystem system, final List<String> serverPaths, final String project, final int timeout) {
        this.system = system;
        this.project = project;
        this.timeout = timeout;
        String filePath = "./config-persistence/" + project + ".cfg";
        configPersistence = new FilePersistence(filePath);
        createLocalCache(serverPaths);
    }

    public String get(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(project).append(":").append(key);
        Future<DataResult<String,String>> future = localArticleCache.ask(new GetData<>(sb.toString()));
        onCompleted(key, future);
        return configPersistence.read(key);
    }

    public String syncGet(String key, long timeout) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(project).append(":").append(key);
        Future<DataResult<String,String>> future = localArticleCache.ask(new GetData<>(sb.toString()));
        onCompleted(key, future);
        return Await.result(future, Duration.create(timeout, "ms")).data;
    }

    private void onCompleted(String key, Future<DataResult<String,String>> future) {
        future.onComplete(
            new OnComplete<DataResult<String,String>>() {
                @Override
                public void onComplete(Throwable ex, DataResult<String,String> result) throws Throwable {
                    if (ex == null && result.failed == null) {
                        if (result.data == null) {
                            logger.error("从配置服务取到的配置项为空串：{}", key);
                        } else {
                            configPersistence.update(key, result.data);
                        }
                    } else {
                        String value = configPersistence.read(key);
                        if (value == null) {
                            logger.error("从配置服务初始化配置失败: {}", key, ex == null ? result.failed : ex);
                        } else {
                            logger.warn("从配置服务更新配置失败: {}, 当前配置: {}", key, value, ex == null ? result.failed : ex);
                        }
                    }
                }
            }, system.dispatcher());
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
