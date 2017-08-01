package net.arksea.config.server.rest;

import net.arksea.config.server.dao.ConfigDocDao;
import net.arksea.config.server.entity.ConfigDoc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Created by xiaohaixing on 2017/7/25.
 */
@RestController
@RequestMapping(value = "/api/v1/configs/doc")
public class ConfigDocController {
    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    private Logger logger = LogManager.getLogger(ConfigDocController.class);

    @Autowired
    private ConfigDocDao configDocDao;

    @RequestMapping(value="{docId}", method = RequestMethod.POST, produces = MEDIA_TYPE)
    public void saveConfigDoc(@RequestBody String configDoc,
                              @PathVariable(name="docId") long docId) {
        logger.info("id={},doc={}",docId,configDoc);
        ConfigDoc doc = new ConfigDoc();
        doc.setId(docId);
        doc.setValue(configDoc);
        doc.setMetadata("abc");
        configDocDao.save(doc);
    }
}
