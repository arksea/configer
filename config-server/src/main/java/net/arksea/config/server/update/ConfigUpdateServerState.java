package net.arksea.config.server.update;

import net.arksea.config.server.login.LoginService;
import net.arksea.config.server.login.TokenService;
import net.arksea.config.server.service.ConfigerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * Created by xiaohaixing on 2019/2/22.
 */
@Component
public class ConfigUpdateServerState {
    @Autowired
    public LoginService loginService;
    @Autowired
    public ConfigerService configerService;
    @Autowired
    TokenService tokenService;
}
