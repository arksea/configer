package net.arksea.config.server.update;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.Props;
import akka.japi.Creator;
import akka.japi.pf.ReceiveBuilder;
import com.auth0.jwt.interfaces.DecodedJWT;
import net.arksea.config.Login;
import net.arksea.config.LoginResult;
import net.arksea.config.ServerResult;
import net.arksea.config.UpdateConfigDoc;
import net.arksea.config.server.entity.User;
import net.arksea.dsf.service.ServiceRequest;
import net.arksea.dsf.service.ServiceResponse;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

/**
 *
 * Created by xiaohaixing on 2019/2/22.
 */
public class ConfigUpdateServer extends AbstractActor {

    private ConfigUpdateServerState state;
    private ConfigUpdateServer(ConfigUpdateServerState state) {
        this.state = state;
    }

    public static Props props(ConfigUpdateServerState state) {
        return Props.create(new Creator<Actor>() {
            @Override
            public Actor create() throws Exception {
                return new ConfigUpdateServer(state);
            }
        });
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(ServiceRequest.class, this::onReceivedServiceRequest)
            .match(Object.class, this::onReceivedObject)
            .build();
    }

    public void onReceivedServiceRequest(ServiceRequest req) {
        onReceived(req.message, req);
    }

    public void onReceivedObject(Object obj) {
        onReceived(obj, null);
    }

    public void onReceived(Object msg, ServiceRequest req) {
        if (msg instanceof UpdateConfigDoc) {
            handleUpdateConfigDoc((UpdateConfigDoc)msg, req);
        } else if (msg instanceof Login) {
            handleLogin((Login)msg, req);
        } else {
            unhandled(msg);
        }
    }

    private void response(ServiceRequest req, ServerResult result) {
        if (req == null) {
            sender().tell(result, self());
        } else {
            sender().tell(new ServiceResponse(result, req), self());
        }
    }

    private void handleUpdateConfigDoc(UpdateConfigDoc msg, ServiceRequest req) {
        try {
            long userId = verifyToken(msg.accessToken);
            if (userId != -1) {
                state.configerService.updateConfigDoc(userId, msg.project, msg.profile, msg.config, msg.value);
                response(req, new ServerResult(0, "succeed"));
            }
        } catch (Exception ex) {
            response(req, new ServerResult(1, ex.getMessage()));
        }
    }

    private void handleLogin(Login msg, ServiceRequest req) {
        try {
            Optional<User> op = state.loginService.login(msg.name, msg.password);
            if (op.isPresent()) {
                User user = op.get();
                Pair<String,Long> token = state.tokenService.create(user.getName(), user.getId());
                response(req, new LoginResult(0, token.getLeft(), token.getRight()));
            } else {
                response(req, new LoginResult(1,"User name or password invalid", 0));
            }
        } catch (Exception ex) {
            response(req, new LoginResult(1, ex.getMessage(), 0));
        }
    }

    private long verifyToken(String token) {
        DecodedJWT jwt = state.tokenService.verify(token);
        if (jwt == null) {
            sender().tell(new ServerResult(401,"Unauthorized"), self());
            return -1;
        }
        return jwt.getClaim("userId").asLong();
    }

}
