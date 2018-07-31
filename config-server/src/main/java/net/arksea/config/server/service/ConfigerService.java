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

    /**
     * 新增配置
     * @param cfg
     * @return 返回增加的配置ID，如果同名配置已存在，则返回 -1
     */
    public Config createConfig(Config cfg, String userName) {
        Config cfg1 = configDao.findByProjectIdAndName(cfg.getProject().getId(), cfg.getName());
        if (cfg1 != null) {
            cfg.setId(cfg1.getId());
            cfg.getDoc().setId(cfg1.getDoc().getId());
        }
        cfg1 = configDao.save(cfg);
        logger.info("create config project={}, profile={}, config={}, userName={}", cfg1.getProject().getName(), cfg1.getProject().getProfile(), cfg1.getName(), userName);
        return cfg1;
    }

    public long createProject(Project prj, String userName) {
        Project prj1 = projectDao.save(prj);
        logger.info("create project name={}, profile={}, desc={}, userName={}",prj1.getName(),prj1.getProfile(), prj1.getDescription(), userName);
        return prj1.getId();
    }

    public void updateConfigDescription(long cfgId, String desc, String userName) {
        configDao.updateDescription(cfgId, desc);
        logger.info("update config description id={}, desc={}, userName={}",cfgId,desc,userName);
    }

    public void updateConfigDoc(long docId, String configDoc, String userName) {
        configDocDao.updateValue(docId, configDoc);
        logger.info("update config value docId={}, value={}, userName={}",docId, configDoc, userName);
    }

    public void updateConfigSchema(long docId, String configSchema, String userName) {
        configDocDao.updateSchema(docId, configSchema);
        logger.info("update config metadata docId={}, userName={}",docId, userName);
    }

    public void deleteConfig(long id, String userName) {
        configDao.deleteById(id);
        logger.info("delete config, Id={}, userName={}", id, userName);
    }

    public void deleteProject(long id, String userName) {
        projectDao.deleteById(id);
        logger.info("delete project, Id={}, userName={}",id, userName);
    }
}
