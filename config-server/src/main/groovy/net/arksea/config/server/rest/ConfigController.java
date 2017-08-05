package net.arksea.config.server.rest;

import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ConfigDocDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.ConfigDoc;
import net.arksea.config.server.service.ConfigerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private ConfigDocDao configDocDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private ConfigerService configerService;

    @RequestMapping(value="/doc/{docId}", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public void saveConfigDoc(@RequestBody String configDoc,
                              @PathVariable(name="docId") long docId) {
        logger.info("save config docId={},doc={},metadata={}",docId,configDoc);
        ConfigDoc doc = new ConfigDoc();
        doc.setId(docId);
        doc.setValue(configDoc);
        doc.setMetadata("json");
        configDocDao.save(doc);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public void insertConfig(@RequestBody Config config) {
        configerService.createConfig(config);
    }
}
