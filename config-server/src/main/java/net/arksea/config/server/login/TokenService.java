package net.arksea.config.server.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 *
 * Created by xiaohaixing on 2017/11/1.
 */
@Component
public class TokenService {
    private static final int COOKIE_EXPIRY = 600000;
    private static final int EXPIRES_SECONDS = 604800; //7天
    private static Logger logger = LogManager.getLogger(TokenService.class);
    private Algorithm algorithm;
    private final String ISSUER = "arksea.net";

    public TokenService() throws UnsupportedEncodingException {
        final String secret = "secret";
        algorithm = Algorithm.HMAC256(secret);
    }

    public int getCookieExpiry() {
        return COOKIE_EXPIRY;
    }
    public String getCookieName() {
        return "access_token";
    }

    public Pair<String,Long> create(String userName) throws JWTCreationException {
        long exp = System.currentTimeMillis()+EXPIRES_SECONDS*1000L;
        String token = JWT.create()
            .withIssuer(ISSUER)
            .withExpiresAt(new Date(exp))
            .withClaim("name", userName)
            .sign(algorithm);
        return Pair.of(token, exp);
    }

    public boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .acceptExpiresAt(EXPIRES_SECONDS)
                .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            Date now = new Date(System.currentTimeMillis());
            return jwt.getExpiresAt().after(now);
        } catch (JWTVerificationException ex){
            logger.warn("token verify failed", ex);
            return false;
        }
    }
}
