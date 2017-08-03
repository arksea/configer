package net.arksea.config.server.system;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import net.arksea.acache.CacheActor;
import net.arksea.acache.ICacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 * Created by xiaohaixing on 2017/07/22.
 */
@Component
public class CacheServerFactory {
    @Autowired
    ActorSystem system;
    @Autowired
    ConfigCacheSource configCacheSource;

    @Bean(name = "cacheServer")
    public ActorRef createCacheServer() {
        ICacheConfig<String> cacheConfig = new ICacheConfig<String>() {
            @Override
            public String getCacheName() {
                return "configCache";
            }
        };
        Props props = CacheActor.props(cacheConfig, configCacheSource);
        return system.actorOf(props, "configCacheServer");
    }
}
