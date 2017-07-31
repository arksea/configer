package net.arksea.config.server.rest;

import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ProjectDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.Project;
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
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ConfigDao configDao;

    @RequestMapping(method = RequestMethod.GET, produces = MEDIA_TYPE)
    public Iterable<Project> listAllProjects() {
        return projectDao.findAll();
    }

    @RequestMapping(value="{prjId}", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public Iterable<Config> listConfigs(@PathVariable(name="prjId") long prjId) {
        return configDao.findByProjectId(prjId);
    }
}
