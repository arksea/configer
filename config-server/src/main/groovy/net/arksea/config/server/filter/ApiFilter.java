package net.arksea.config.server.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaohaixing
 */
@Component("apiFilter")
public class ApiFilter implements Filter {
    @Autowired
    private Environment env;

    @Override
    public void destroy() {
        //do nothing
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse resp, final FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        String profile = env.getProperty("spring.profiles.active");
        if (!"product".equals(profile)) { //测试状态下允许跨域访问，方便用ng server测试
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setHeader("Access-Control-Allow-Methods","POST,GET");
        }
        chain.doFilter(request, resp);
    }

    @Override
    public void init(final FilterConfig config) throws ServletException {
        //do nothing
    }

}
