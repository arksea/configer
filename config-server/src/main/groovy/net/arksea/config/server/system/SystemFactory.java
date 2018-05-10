package net.arksea.config.server.system;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.arksea.dsf.register.RegisterClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;


/**
 *
 * Created by xiaohaixing on 2017/07/22.
 */
@Component
public class SystemFactory {
    private Logger logger = LogManager.getLogger(SystemFactory.class);
    Config config;
    ActorSystem system;
    @Autowired
    RegisterClient registerClient;

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
        system = ActorSystem.create("system",config);
        return system;
    }

    @PreDestroy
    public void stop() {
        try {
            logger.info("Stopping config server system");
            Future f = system.terminate();
            Await.result(f, Duration.apply(10, TimeUnit.SECONDS));
            logger.info("Config server system stopped");
        } catch (Exception e) {
            logger.warn("Stop config server system timeout", e);
        }
        registerClient.stopAndWait(10);
    }
}

