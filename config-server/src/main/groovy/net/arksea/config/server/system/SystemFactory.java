package net.arksea.config.server.system;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 *
 * Created by xiaohaixing on 2017/07/22.
 */
@Component
public class SystemFactory {
    Config config;
    public SystemFactory() {
        Config cfg = ConfigFactory.load();
        config = cfg.getConfig("system").withFallback(cfg);

    }

    @Bean(name = "systemConfig")
    Config createConfig() {

        return config;
    }

    @Bean(name = "system")
    public ActorSystem createSystem() {
        return ActorSystem.create("system",config);
    }
}
