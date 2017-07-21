package net.arksea.config;

import akka.actor.ActorSystem;
import net.arksea.acache.*;
import scala.concurrent.Future;

import java.io.File;
import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/7/12.
 */
public class ConfigService {
    public final ActorSystem system;
    private final int timeout;
    private final String project;
    private CacheAsker<String,String> localArticleCache;
    private IConfigPersistence configPersistence;
    public ConfigService(final ActorSystem system, final List<String> serverPaths, final String project, final int timeout) {
        this.system = system;
        this.project = project;
        this.timeout = timeout;
        String filePath = "./config-persistence/"+project+".cfg";
        configPersistence = new FilePersistence(filePath);
        createLocalCache(serverPaths);
    }

    public Future<DataResult<String,String>> ask(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(project).append(":").append(key);
        return localArticleCache.ask(new GetData<>(sb.toString()));
    }

    public String get(String key) throws CacheAskException {
        return localArticleCache.get(key);
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
