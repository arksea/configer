package net.arksea.config.server.system;

import net.arksea.dsf.register.RegisterClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by xiaohaixing on 2018/5/9.
 */
@Component
public class RegisterFactory {
    @Value("${dsf.registerAddrs}")
    String dsfRegisterAddrs;

    @Value("${dsf.clientName}")
    String dsfClientName;

    @Bean
    RegisterClient create() {
        return new RegisterClient(dsfClientName, dsfRegisterAddrs);
    }
}
