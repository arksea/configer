package net.arksea.config.server.service;

import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ConfigDocDao;
import net.arksea.config.server.dao.ProjectDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 *
 * Created by xiaohaixing on 2017/8/5.
 */
@Component
@Transactional
public class ConfigerService {
    private Logger logger = LogManager.getLogger(ConfigerService.class);

    @Autowired
    private ConfigDocDao configDocDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private ProjectDao projectDao;

    public void createConfig(Config cfg) {
        Config cfg1 = configDao.save(cfg);
        logger.info("create config {}:{}:{}",cfg1.getProject().getName(),cfg1.getProject().getProfile(),cfg1.getName());
    }

    public void createProject(Project prj) {
        Project prj1 = projectDao.save(prj);
        logger.info("create project name={}, profile={}, desc={}",prj1.getName(),prj1.getProfile(), prj1.getDescription());
    }

    public void updateConfigDescription(long cfgId, String desc) {
        configDao.updateDescription(cfgId, desc);
        logger.info("update config description id={}, desc={}",cfgId,desc);
    }

    public void updateConfigDoc(long docId, String configDoc) {
        configDocDao.updateValue(docId, configDoc);
        logger.info("update config value docId={}, value={}",docId, configDoc);
    }

    public void updateConfigSchema(long docId, String configSchema) {
        configDocDao.updateSchema(docId, configSchema);
        logger.info("update config metadata docId={}, schema={}",docId, configSchema);
    }
}
