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
    @Bean(name = "system")
    public ActorSystem createSystem() {
        Config config = ConfigFactory.load();
        return ActorSystem.create("system",config.getConfig("system").withFallback(config));
    }
}
