package net.arksea.config.server.rest;

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

    @Autowired
    private ConfigDao configDao;

    @RequestMapping(params={"name","projectId"},method = RequestMethod.GET, produces = MEDIA_TYPE)
    public DeferredResult<String> getConfig(@RequestParam(name="name") String name,
                            @RequestParam(name="projectId") long projectId,
                            final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Config cfg = configDao.findByProjectIdAndName(projectId, name);
        result.setResult(RestUtils.createResult(0, cfg, reqid));
        return result;
    }

    @RequestMapping(value="/{configId}/description", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public DeferredResult<String> updateConfigDesc(@RequestBody String configDesc,
                                                   @PathVariable(name="configId") long configId,
                                                   final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        String userName = (String)httpRequest.getAttribute("jwt-user-name");
        configerService.updateConfigDescription(configId, configDesc, userName);
        result.setResult(RestUtils.createResult(0, "succeed", reqid));
        return result;
    }

    @RequestMapping(value="/{configId}/docs/{docId}", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public DeferredResult<String> updateConfigDoc(@RequestBody String configDoc,
                                                  @PathVariable(name="docId") long docId,
                                                  final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        String userName = (String)httpRequest.getAttribute("jwt-user-name");
        configerService.updateConfigDoc(docId, configDoc, userName);
        result.setResult(RestUtils.createResult(0, "succeed", reqid));
        return result;
    }

    @RequestMapping(value="/{configId}/schema/{docId}", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public DeferredResult<String> updateConfigSchema(@RequestBody String configSchema,
                                   @PathVariable(name="docId") long docId,
                                   final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        String userName = (String)httpRequest.getAttribute("jwt-user-name");
        configerService.updateConfigSchema(docId, configSchema, userName);
        result.setResult(RestUtils.createResult(0, "succeed", reqid));
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> createConfig(@RequestBody Config config, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        String userName = (String)httpRequest.getAttribute("jwt-user-name");
        Config cfg1 = configerService.createConfig(config, userName);
        result.setResult(RestUtils.createResult(0, cfg1, reqid));
        return result;
    }

    @RequestMapping(value="/{configId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public DeferredResult<String> deleteConfig(@PathVariable(name="configId") long configId, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        String userName = (String)httpRequest.getAttribute("jwt-user-name");
        configerService.deleteConfig(configId, userName);
        result.setResult(RestUtils.createResult(0, "succeed", reqid));
        return result;
    }
}
