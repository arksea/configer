package net.arksea.config.server.filter;

import net.arksea.config.server.login.TokenService;
import net.arksea.restapi.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * @author xiaohaixing
 */
@Component("apiFilter")
public class ApiFilter implements Filter {
    @Autowired
    private Environment env;

    @Autowired
    TokenService tokenService;

    @Override
    public void destroy() {
        //do nothing
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse resp, final FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Headers","Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials","true");
        String profile = env.getProperty("spring.profiles.active");
        if (!"product".equals(profile)) {
            //测试状态下允许所有方法跨域访问，方便用ng server测试
            response.setHeader("Access-Control-Allow-Origin","http://127.0.0.1:4200");
            response.setHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, PATCH");
        }
        HttpServletRequest req = (HttpServletRequest) request;
        String reqid = req.getHeader("restapi-requestid");
        if (reqid == null) {
            reqid = UUID.randomUUID().toString();
        }
        req.setAttribute("restapi-requestid", reqid);
        String uri = req.getRequestURI();
        if ("/heartbeat".equals(uri) || //haproxy用于监测站点状态的请求，不做校验
            "/api/v1/login".equals(uri) ||
            "/api/v1/signup".equals(uri) ||
            req.getMethod().equals(RequestMethod.OPTIONS.name())) { //跨域OPTIONS请求不校验
            chain.doFilter(req, resp);
        } else if (verifyToken(req)) {
            chain.doFilter(request, resp);
        } else {
            this.resultError(HttpStatus.UNAUTHORIZED, 1, "访问未授权", req, response);
        }
    }

    private boolean verifyToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("access_token".equals(c.getName()) && tokenService.verify(c.getValue())) {
                    return true;
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
