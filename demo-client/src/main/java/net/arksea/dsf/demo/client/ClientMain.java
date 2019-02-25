package net.arksea.dsf.demo.client;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.arksea.config.ConfigService;
import net.arksea.config.ConfigUpdateService;
import net.arksea.dsf.client.Client;
import net.arksea.dsf.register.RegisterClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by xiaohaixing on 2018/04/17.
 */
public final class ClientMain {
    private static final Logger logger = LogManager.getLogger(ClientMain.class);
    private ClientMain() {};

    /**
     * @param args command line args
     */
    public static void main(final String[] args) {
        testEnableRegister();
    }

    public static void test() {
        try {
            logger.info("Start DEMO Client");
            Config cfg = ConfigFactory.parseResources("default-system.conf");
            ActorSystem system = akka.actor.ActorSystem.create("configServiceSystem", cfg);
            ConfigService configService = new ConfigService(
                "172.17.150.87:8806",
                "net.arksea.TestProject", "online",
                5000, system);
            String value = configService.getString("app.init.configer1");
            logger.info(value);

            ConfigUpdateService updateSvc = new ConfigUpdateService(
                "xiaohaixing","123456",
                "172.17.150.87:8806",
                "net.arksea.TestProject", "online",
                5000, system);
            updateSvc.update("appBoot.testConfig3", "\""+LocalDateTime.now().toString()+"\"");

            Thread.sleep(3000);
        } catch (Exception ex) {
            logger.error("DEMO Client failed", ex);
        }
    }

    public static void testEnableRegister() {
        try {
            logger.info("Start DEMO Client");
            List<String> registerAddrs = new LinkedList<>();
            registerAddrs.add("127.0.0.1:6501");
            registerAddrs.add("127.0.0.1:6502");
            RegisterClient register = new RegisterClient("configer-demo-client", registerAddrs);
            Client client = register.subscribe("net.arksea.ConfigServer-DEV");
            ConfigService configService = new ConfigService(client, "net.arksea.TestProject", "online", 5000, client.system);
            String value = configService.getString("app.init.configer1");
            logger.info(value);

            Thread.sleep(3000);

            Client updateClient = register.subscribe("net.arksea.ConfigUpdateServer-DEV");
            ConfigUpdateService updateSvc = new ConfigUpdateService(
                "xiaohaixing", "123456",
                 updateClient,
                "net.arksea.TestProject", "online",
                5000);
            updateSvc.update("appBoot.testConfig3", "\""+LocalDateTime.now().toString()+"\"");



            Thread.sleep(3000);
        } catch (Exception ex) {
            logger.error("DEMO Client failed", ex);
        }
    }
}
