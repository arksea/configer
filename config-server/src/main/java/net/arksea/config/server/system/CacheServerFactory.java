package net.arksea.config.server.system;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import net.arksea.acache.CacheActor;
import net.arksea.acache.ICacheConfig;
import net.arksea.config.ConfigKey;
import net.arksea.config.RegisterName;
import net.arksea.dsf.codes.ICodes;
import net.arksea.dsf.codes.JavaSerializeCodes;
import net.arksea.dsf.register.RegisterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
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

    @Value("${config.cache.actorCount:1}")
    int cacheActorCount;

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
        Props props;
        if (cacheActorCount > 1) {
            props = CacheActor.propsOfCachePool(cacheActorCount, cacheConfig, configCacheSource);
        } else {
            props = CacheActor.props(cacheConfig, configCacheSource);
        }
        ActorRef actorRef = system.actorOf(props, "configCacheServer");
        int bindPort = systemConfig.getInt("akka.remote.netty.tcp.port");
        String regname = RegisterName.configServer;
        if (!serverProfile.equals("online")) {
            regname = RegisterName.configServer + "-" + serverProfile;
        }
        if (registerClient != null) {
            String hostAddrss = InetAddress.getLocalHost().getHostAddress();
            ICodes codes = new JavaSerializeCodes();
            registerClient.register(regname, hostAddrss, bindPort, actorRef, system, codes);
        }
        return actorRef;
    }
}
