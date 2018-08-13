package net.arksea.config.server.rest;

import net.arksea.config.server.ResultCode;
import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.service.ConfigerService;
import net.arksea.restapi.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 *
 * Created by xiaohaixing on 2017/7/25.
 */
@RestController
@RequestMapping(value = "/api/v1/configs")
public class ConfigController {
    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    private Logger logger = LogManager.getLogger(ConfigController.class);

    @Autowired
    private ConfigerService configerService;

    @RequestMapping(params={"project","profile","config"},method = RequestMethod.GET, produces = MEDIA_TYPE)
    public DeferredResult<String> getConfig(@RequestParam(name="config") String config,
                            @RequestParam(name="project") String project,
                            @RequestParam(name="profile") String profile,
                            final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        Optional<Config> op = configerService.getProjectConfig(userId, project, profile, config);
        if (op.isPresent()) {
            result.setResult(RestUtils.createResult(ResultCode.SUCCEED, op.get(), reqid));
        } else {
            result.setResult(RestUtils.createError(ResultCode.NOT_EXISTS, "未找到指定配置", reqid));
        }
        return result;
    }

    @RequestMapping(value="/{configId}/description", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public DeferredResult<String> updateConfigDesc(@RequestBody String configDesc,
                                                   @PathVariable(name="configId") long configId,
                                                   final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        configerService.updateConfigDescription(userId, configId, configDesc);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, "succeed", reqid));
        return result;
    }

    @RequestMapping(value="/{configId}/docs/{docId}", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public DeferredResult<String> updateConfigDoc(@RequestBody String configDoc,
                                                  @PathVariable(name="docId") long docId,
                                                  final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        configerService.updateConfigDoc(userId, docId, configDoc);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, "succeed", reqid));
        return result;
    }

    @RequestMapping(value="/{configId}/schema/{docId}", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public DeferredResult<String> updateConfigSchema(@RequestBody String configSchema,
                                                     @PathVariable(name="docId") long docId,
                                                     final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        configerService.updateConfigSchema(userId, docId, configSchema);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, "succeed", reqid));
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> createConfig(@RequestBody Config config, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        Config cfg1 = configerService.createConfig(userId, config);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, cfg1, reqid));
        return result;
    }

    @RequestMapping(value="/{configId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public DeferredResult<String> deleteConfig(@PathVariable(name="configId") long configId, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        configerService.deleteConfig(userId, configId);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, "succeed", reqid));
        return result;
    }
}
