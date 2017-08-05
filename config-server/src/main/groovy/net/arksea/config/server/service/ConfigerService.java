package net.arksea.config.server.service;

import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ConfigDocDao;
import net.arksea.config.server.entity.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
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

    public void createConfig(Config cfg) {
        Config cfg1 = configDao.save(cfg);
        logger.info("insert config {}:{}:{}",cfg1.getProject().getName(),cfg1.getProject().getProfile(),cfg1.getName());
    }
}
