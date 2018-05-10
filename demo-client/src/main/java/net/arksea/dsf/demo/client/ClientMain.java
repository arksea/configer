package net.arksea.dsf.demo.client;

import net.arksea.config.ConfigService;
import net.arksea.dsf.client.Client;
import net.arksea.dsf.register.RegisterClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        try {
            logger.info("Start DEMO Client");
            String configServiceName = "net.arksea.ConfigServer";
            RegisterClient register = new RegisterClient("TestClient","10.79.186.111:8877");
            Client client = register.subscribe(configServiceName);
            ConfigService configService = new ConfigService(client, "weather-api", "QA", 5000, client.system);
            //Thread.sleep(1000);
            for (int i=0; i<1000000; ++i) {
                try {
                    String value = configService.getString("weather-data.service.addr");
                    logger.info(value);
                } catch (Exception ex) {
                    logger.warn("read config failed", ex);
                }
                Thread.sleep(10000);
            }
            Thread.sleep(10000);
            register.stop().value();
            client.system.terminate().value();
        } catch (Exception ex) {
            logger.error("Start DEMO Client failed", ex);
        }
    }
}
