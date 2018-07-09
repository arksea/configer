package net.arksea.dsf.demo.client;

import net.arksea.config.ConfigService;
import net.arksea.dsf.client.Client;
import net.arksea.dsf.register.RegisterClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        test();
    }

    public static void test() {
        try {
            logger.info("Start DEMO Client");
            ConfigService configService = new ConfigService("172.17.150.87:8806", "net.arksea.TestProject", "online");
            try {
                String value = configService.getString("app.init.configer1");
                logger.info(value);
            } catch (Exception ex) {
                logger.warn("read config failed", ex);
            }
            Thread.sleep(3000);
            configService.system.terminate().value();
        } catch (Exception ex) {
            logger.error("Start DEMO Client failed", ex);
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
            try {
                String value = configService.getString("app.init.configer1");
                logger.info(value);
            } catch (Exception ex) {
                logger.warn("read config failed", ex);
            }
            Thread.sleep(3000);
            register.stop().value();
            client.system.terminate().value();
        } catch (Exception ex) {
            logger.error("Start DEMO Client failed", ex);
        }
    }
}
