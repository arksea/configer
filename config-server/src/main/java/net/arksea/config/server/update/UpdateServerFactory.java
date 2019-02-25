package net.arksea.config.server.update;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
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
 * Created by xiaohaixing on 2019/2/22.
 */
@Component
public class UpdateServerFactory {
    @Autowired
    ActorSystem system;

    @Autowired
    RegisterClient registerClient;

    @Resource(name="systemConfig")
    Config systemConfig;

    @Resource(name="serverProfile")
    String serverProfile;

    @Autowired
    ConfigUpdateServerState state;

    @Bean(name = "configUpdateServer")
    public ActorRef createConfigUpdateServer() throws UnknownHostException {
        Props props = ConfigUpdateServer.props(state);
        ActorRef actorRef = system.actorOf(props, "configUpdateServer");
        int bindPort = systemConfig.getInt("akka.remote.netty.tcp.port");
        String regname = RegisterName.configUpdateServer;
        if (!serverProfile.equals("online")) {
            regname = RegisterName.configUpdateServer + "-" + serverProfile;
        }
        if (registerClient != null) {
            String hostAddrss = InetAddress.getLocalHost().getHostAddress();
            ICodes codes = new JavaSerializeCodes();
            registerClient.register(regname, hostAddrss, bindPort, actorRef, system, codes);
        }
        return actorRef;
    }
}
