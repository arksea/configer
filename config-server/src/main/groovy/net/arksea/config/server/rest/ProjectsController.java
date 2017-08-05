package net.arksea.config.server.rest;

import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ConfigDocDao;
import net.arksea.config.server.dao.ProjectDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.ConfigDoc;
import net.arksea.config.server.entity.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Created by xiaohaixing on 2017/7/25.
 */
@RestController
@RequestMapping(value = "/api/v1/projects")
public class ProjectsController {
    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    private Logger logger = LogManager.getLogger(ProjectsController.class);
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private ConfigDocDao configDocDao;

    @RequestMapping(method = RequestMethod.GET, produces = MEDIA_TYPE)
    public Iterable<Project> listAllProjects() {
        Iterable<Project> projects = projectDao.findAll();
        projects.forEach(it -> logger.info(it.getName()));
        return projects;
    }

    @RequestMapping(value="{prjId}", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public Iterable<Config> listConfigs(@PathVariable(name="prjId") long prjId) {
        Iterable<Config> configs = configDao.findByProjectId(prjId);
        configs.forEach(it -> logger.info(it.getName()));
        return configs;
    }
}