package net.arksea.config;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.pattern.Patterns;
import net.arksea.dsf.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static akka.japi.Util.classTag;

/**
 *
 * Created by xiaohaixing on 2019/02/22.
 */
public class ConfigUpdateService {
    private static Logger logger = LogManager.getLogger(ConfigUpdateService.class);
    public  final String project;
    public  final String profile;
    public  final int timeout;
    public  final ActorSystem system;
    private final ActorSelection actor;
    private final Client dsfClient;
    private String accessToken;
    private long tokenExpiredTime;
    private String userName;
    private String password;


    public ConfigUpdateService(final String userName,
                               final String password,
                               final Client dsfClient,
                               final String project,
                               final String profile,
                               final int timeout) {
        this.project = project;
        this.profile = profile;
        this.timeout = timeout;
        this.userName = userName;
        this.password = password;
        this.dsfClient = dsfClient;
        this.system = this.dsfClient.system;
        this.actor = null;
        try {
            login();
        } catch (Exception e) {
            logger.warn("Login to config server failed",e);
        }
    }

    public ConfigUpdateService(final String userName,
                               final String password,
                               final String serverAddr,
                               final String project,
                               final String profile,
                               final int timeout,
                               final ActorSystem system) {
        this.project = project;
        this.profile = profile;
        this.timeout = timeout;
        this.userName = userName;
        this.password = password;
        this.dsfClient = null;
        this.system = system;
        this.actor = system.actorSelection("akka.tcp://system@"+serverAddr+"/user/configUpdateServer");
        try {
            login();
        } catch (Exception e) {
            logger.warn("Login to config server failed",e);
        }
    }

    public Future<ServerResult> update(String configName, String value) {
        try {
            login();
            UpdateConfigDoc msg = new UpdateConfigDoc(accessToken, project, profile, configName, value);
            if (actor == null) {
                return dsfClient.request(msg, timeout).mapTo(classTag(ServerResult.class));
            } else {
                return Patterns.ask(actor, msg, timeout).mapTo(classTag(ServerResult.class));
            }
        } catch (Exception ex) {
            return Futures.successful(new ServerResult(1, ex.getMessage()));
        }
    }

    private void login() throws Exception {
        if (System.currentTimeMillis() + 3600_000 > this.tokenExpiredTime) {
            Duration duration = Duration.create(timeout, "ms");
            Login msg = new Login(userName, password);
            Future<LoginResult> future;
            if (actor == null) {
                future = dsfClient.request(msg,timeout).mapTo(classTag(LoginResult.class));
            } else {
                future = Patterns.ask(actor, msg, timeout).mapTo(classTag(LoginResult.class));
            }
            LoginResult ret = Await.result(future, duration);
            if (ret.code == 0) {
                this.accessToken = ret.message;
                this.tokenExpiredTime = ret.expiredTime;
            } else {
                throw new Exception(ret.message);
            }
        }
    }

}
