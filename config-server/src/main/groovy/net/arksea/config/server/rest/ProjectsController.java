package net.arksea.config.server.rest;

import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ConfigDocDao;
import net.arksea.config.server.dao.ProjectDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.ConfigDoc;
import net.arksea.config.server.entity.Project;
import net.arksea.config.server.service.ConfigerService;
import net.arksea.restapi.RestUtils;
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
    @Autowired
    private ConfigerService configerService;

    @RequestMapping(method = RequestMethod.GET, produces = MEDIA_TYPE)
    public Iterable<Project> listAllProjects() {
        Iterable<Project> projects = projectDao.findAll();
        projects.forEach(it -> logger.info(it.getName()));
        return projects;
    }

    @RequestMapping(method = RequestMethod.GET, params={"name","profile"}, produces = MEDIA_TYPE)
    public Project getProject(@RequestParam(name="name") String name,
                              @RequestParam(name="profile") String profile) {
        return projectDao.getByNameAndProfile(name, profile);
    }

    @RequestMapping(value="{prjId}", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public Project getProjectById(@PathVariable(name="prjId") long prjId) {
        return projectDao.findOne(prjId);
    }

    @RequestMapping(value="{prjId}/configs", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public Iterable<Config> listConfigs(@PathVariable(name="prjId") long prjId) {
        return configDao.findByProjectId(prjId);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> createProject(@RequestBody Project project, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        long prjId = configerService.createProject(project);
        result.setResult(RestUtils.createJsonResult(0, prjId, reqid));
        return result;
    }

    @RequestMapping(value="/{projectId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public DeferredResult<String> deleteProject(@PathVariable(name="projectId") long projectId, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        configerService.deleteProject(projectId);
        result.setResult(RestUtils.createResult(0, "succeed", reqid));
        return result;
    }
}