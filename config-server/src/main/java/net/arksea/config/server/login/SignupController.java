package net.arksea.config.server.login;

import net.arksea.restapi.RestUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Created by xiaohaixing on 2017/10/31.
 */
@RestController
@RequestMapping(value = "/api/v1/signup")
public class SignupController {
    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    private static Logger logger = LogManager.getLogger(SignupController.class);
    @Autowired
    private TokenService tokenService;
    @Autowired
    private LoginService loginService;

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> signup(@RequestBody final SignupInfo info,
                         final HttpServletRequest httpRequest,
                         final HttpServletResponse httpResponse) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        SignupStatus code = loginService.signup(info);
        switch (code) {
            case SUCCEED:
                Pair<String,Long> token = tokenService.create(info.getName());
                Cookie c = new Cookie(tokenService.getCookieName(), token.getLeft());
                c.setMaxAge(tokenService.getCookieExpiry());
                c.setHttpOnly(true);
                httpResponse.addCookie(c);
                result.setResult(RestUtils.createResult(0, "succeed", reqid));
                break;
            case USERNAME_EXISTS:
                Cookie c1 = new Cookie(tokenService.getCookieName(), null);
                c1.setMaxAge(0);
                httpResponse.addCookie(c1);
                result.setResult(RestUtils.createError(code.getValue(), "user name exists", reqid));
                break;
            default:
                Cookie c2 = new Cookie(tokenService.getCookieName(), null);
                c2.setMaxAge(0);
                httpResponse.addCookie(c2);
                result.setResult(RestUtils.createError(code.getValue(), "signup failed", reqid));
                break;
        }
        return result;
    }
}
