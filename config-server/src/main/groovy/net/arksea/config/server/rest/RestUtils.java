package net.arksea.config.server.rest;

import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;

//import javax.servlet.http.HttpSession;

/**
 *
 * Created by xiaohaixing_dian91 on 2016/11/25.
 */
public final class RestUtils {
    private RestUtils() {
    }
    public static String getRequestLogInfo(final WebRequest req) {
        final StringBuilder sb = new StringBuilder();
        fillRequestLogInfo(sb, req);
        return sb.toString();
    }

    public static void fillRequestLogInfo(final StringBuilder sb, final WebRequest req) {
        sb.append("request: ").append(req.getDescription(true));
        sb.append("\nparams: \n");
        Iterator<String> it = req.getParameterNames();
        while (it.hasNext()) {
            String name = it.next();
            String value = req.getParameter(name);
            sb.append("  ").append(name).append(": ").append(value).append("\n");
        }
        sb.append("headers: \n");
        it = req.getHeaderNames();
        while(it.hasNext()) {
            String name = it.next();
            String value = req.getHeader(name);
            sb.append("  ").append(name).append(": ").append(value).append("\n");
        }
    }

    public static String getRequestLogInfo(final HttpServletRequest req) {
        final StringBuilder sb = new StringBuilder();
        fillRequestLogInfo(sb,req);
        return sb.toString();
    }
    public static void fillRequestLogInfo(final StringBuilder sb, final HttpServletRequest req) {
        sb.append("request: uri=").append(req.getRequestURI())
          .append(";client=").append(req.getRemoteAddr());
//        HttpSession session = req.getSession();
//        if (session != null) {
//            sb.append(";sessionId=").append(session.getId());
//        }
        sb.append("\nparams: \n");
        Enumeration<String> it = req.getParameterNames();
        while (it.hasMoreElements()) {
            String name = it.nextElement();
            String value = req.getParameter(name);
            sb.append("  ").append(name).append(": ").append(value).append("\n");
        }
        sb.append("headers: \n");
        it = req.getHeaderNames();
        while(it.hasMoreElements()) {
            String name = it.nextElement();
            String value = req.getHeader(name);
            sb.append("  ").append(name).append(": ").append(value).append("\n");
        }
    }
}
