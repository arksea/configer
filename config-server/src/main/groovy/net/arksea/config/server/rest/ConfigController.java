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
    public Config getConfig(@RequestParam(name="name") String name,
                            @RequestParam(name="projectId") long projectId) {
        return configDao.findByProjectIdAndName(projectId, name);
    }

    @RequestMapping(value="/{configId}/description", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public void updateConfigDesc(@RequestBody String configDesc,
                                 @PathVariable(name="configId") long configId) {
        configerService.updateConfigDescription(configId, configDesc);
    }

    @RequestMapping(value="/{configId}/docs/{docId}", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public void updateConfigDoc(@RequestBody String configDoc,
                              @PathVariable(name="docId") long docId) {
        configerService.updateConfigDoc(docId, configDoc);
    }

    @RequestMapping(value="/{configId}/schema/{docId}", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public void updateConfigSchema(@RequestBody String configSchema,
                                   @PathVariable(name="docId") long docId) {
        configerService.updateConfigSchema(docId, configSchema);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> createConfig(@RequestBody Config config, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Config cfg1 = configerService.createConfig(config);
        result.setResult(RestUtils.createJsonResult(0, cfg1, reqid));
        return result;
    }

    @RequestMapping(value="/{configId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public DeferredResult<String> deleteConfig(@PathVariable(name="configId") long configId, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        configerService.deleteConfig(configId);
        result.setResult(RestUtils.createResult(0, "succeed", reqid));
        return result;
    }
}
