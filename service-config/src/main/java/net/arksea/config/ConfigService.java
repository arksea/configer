package net.arksea.config;

import akka.actor.ActorSystem;
import net.arksea.acache.*;
import scala.concurrent.Future;

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
    public ConfigService(final ActorSystem system, final List<String> serverPaths, final String project, final int timeout) {
        this.system = system;
        this.project = project;
        this.timeout = timeout;
        createLocalCache(serverPaths);
    }

    public Future<DataResult<String,String>> get(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(project).append(":").append(key);
        return localArticleCache.ask(new GetData<>(sb.toString()));
    }

    private void createLocalCache(final List<String> serverPaths) {
        ICacheConfig<String> cfg = new ICacheConfig<String>() {
            @Override
            public String getCacheName() {
                return "localConfigCache";
            }
        };
        localArticleCache = LocalCacheCreator.createLocalCache(system, cfg, serverPaths, timeout);
    }
}
