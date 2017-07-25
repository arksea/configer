package net.arksea.config.server.rest;

import net.arksea.config.server.dao.ProjectDao;
import net.arksea.config.server.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * Created by xiaohaixing on 2017/7/25.
 */
@RestController
@RequestMapping(value = "/api/v1/projects")
public class ProjectsController {
    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    @Autowired
    ProjectDao projectDao;
    @RequestMapping(method = RequestMethod.GET, produces = MEDIA_TYPE)
    public Iterable<Project> list() {
        return projectDao.findAll();
    }
}
