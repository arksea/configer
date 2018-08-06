package net.arksea.config.server.login;

import net.arksea.config.server.ResultCode;
import net.arksea.config.server.entity.User;
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
import java.util.Optional;

/**
 *
 * Created by xiaohaixing on 2017/10/31.
 */
@RestController
@RequestMapping(value = "/api/v1/login")
public class LoginController {
    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    private Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    private LoginService loginService;

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> login(@RequestBody final LoginInfo body,
                                        final HttpServletRequest httpRequest,
                                        final HttpServletResponse httpResponse) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Optional<User> op = loginService.login(body);
        if (op.isPresent()) {
            User user = op.get();
            Pair<String,Long> token = tokenService.create(user.getName(), user.getId());
            Cookie c = new Cookie(tokenService.getCookieName(), token.getLeft());
            c.setMaxAge(tokenService.getCookieExpiry());
            c.setHttpOnly(true);
            httpResponse.addCookie(c);
            result.setResult(RestUtils.createResult(ResultCode.SUCCEED, token.getRight(), reqid));
        } else {
            Cookie c1 = new Cookie(tokenService.getCookieName(), null);
            c1.setMaxAge(0);
            httpResponse.addCookie(c1);
            result.setResult(RestUtils.createError(ResultCode.FAILED, "Invalid user name or password", reqid));
        }
        return result;
    }

}
