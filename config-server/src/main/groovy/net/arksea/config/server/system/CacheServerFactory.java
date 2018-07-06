package net.arksea.config.server.system;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import net.arksea.acache.CacheActor;
import net.arksea.acache.ICacheConfig;
import net.arksea.config.ConfigKey;
import net.arksea.dsf.register.RegisterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.UnknownHostException;

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

    @Value("${config.serviceRegisterName}")
    String serviceRegisterName;

    @Autowired
    RegisterClient registerClient;

    @Resource(name="systemConfig")
    Config systemConfig;

    @Resource(name="serverProfile")
    String serverProfile;

    @Bean(name = "cacheServer")
    public ActorRef createCacheServer() throws UnknownHostException {
        ICacheConfig<ConfigKey> cacheConfig = new ICacheConfig<ConfigKey>() {
            @Override
            public String getCacheName() {
                return "configCache";
            }
            @Override
            public boolean waitForRespond() {
                return true;
            }
        };
        Props props = CacheActor.props(cacheConfig, configCacheSource);
        ActorRef actorRef = system.actorOf(props, "configCacheServer");
        int bindPort = systemConfig.getInt("akka.remote.netty.tcp.port");
        String regname = serviceRegisterName;
        if (!serverProfile.equals("online")) {
            regname = serviceRegisterName + "-" + serverProfile;
        }
        registerClient.register(regname, bindPort, actorRef, system);
        return actorRef;
    }
}
