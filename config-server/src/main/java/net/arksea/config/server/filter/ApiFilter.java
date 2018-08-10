package net.arksea.config.server.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import net.arksea.config.server.ResultCode;
import net.arksea.config.server.login.TokenService;
import net.arksea.restapi.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.logging.annotations.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author xiaohaixing
 */
@Component("apiFilter")
public class ApiFilter implements Filter {
    private static Logger logger = LogManager.getLogger(ApiFilter.class);
    @Autowired
    private Environment env;

    @Autowired
    TokenService tokenService;

    @Value("${config.web.allowedOrigins}")
    String allowOriginsStr;
    private Set<String> allowOrigins;

    @Override
    public void destroy() {
        //do nothing
    }

    @PostConstruct
    public void initComponent() {
        logger.debug("allowedOriginsStr: {}", allowOriginsStr);
        String[] origins = StringUtils.split(allowOriginsStr,',');
        allowOrigins = new HashSet<>();
        for (String o : origins) {
            allowOrigins.add(o);
        }
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse resp, final FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest req = (HttpServletRequest) request;
        response.setHeader("Access-Control-Allow-Headers","Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials","true");
        String origin = req.getHeader("Origin");
        if(origin == null) {
            origin = req.getHeader("Referer");
        }
        if (allowOrigins.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        String profile = env.getProperty("spring.profiles.active");
        if (profile != null && !"production".equals(profile)) {
            //测试状态下允许所有方法跨域访问，方便用ng server测试
            response.setHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, PATCH");
        } else { //允许跨域访问（weather-config-web需要调用config接口）
            response.setHeader("Access-Control-Allow-Methods","GET, PUT");
        }

        String reqid = req.getHeader("restapi-requestid");
        if (reqid == null) {
            reqid = UUID.randomUUID().toString();
        }
        req.setAttribute("restapi-requestid", reqid);
        String uri = req.getRequestURI();
        if (!uri.startsWith("/api/v1") ||
            "/api/v1/login".equals(uri) ||
            "/api/v1/signup".equals(uri) ||
            req.getMethod().equals(RequestMethod.OPTIONS.name())) { //跨域OPTIONS请求不校验
            chain.doFilter(req, resp);
        } else if (verifyToken(req)) {
            chain.doFilter(request, resp);
        } else {
            this.resultError(HttpStatus.UNAUTHORIZED, ResultCode.TOKEN_EXPIRED, "Unauthorized", req, response);
        }
    }

    private boolean verifyToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("access_token".equals(c.getName())) {
                    DecodedJWT jwt = tokenService.verify(c.getValue());
                    if (jwt != null) {
                        req.setAttribute("jwt-user-name", jwt.getClaim("userName").asString());
                        req.setAttribute("jwt-user-id", jwt.getClaim("userId").asLong());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void resultError(HttpStatus status, int errCode, String error, HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {
        PrintWriter out = null;
        try {
            httpResponse.setStatus(status.value());
            out = httpResponse.getWriter();
            String reqid = (String)request.getAttribute("restapi-requestid");
            String data = RestUtils.createError(errCode, error, reqid);
            out.write(data);
            out.flush();
        } finally {
            if(out != null) {
                out.close();
            }
        }
    }

    @Override
    public void init(final FilterConfig config) throws ServletException {
        //do nothing
    }

}
