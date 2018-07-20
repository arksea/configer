package net.arksea.config.server.rest;

import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ConfigDocDao;
import net.arksea.config.server.dao.ProjectDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.Project;
import net.arksea.config.server.service.ConfigerService;
import net.arksea.restapi.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public DeferredResult<String> listAllProjects(final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        List<Project> projects = projectDao.getAllNotDeleted();
        result.setResult(RestUtils.createResult(0, projects, reqid));
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, params={"name","profile"}, produces = MEDIA_TYPE)
    public DeferredResult<String> getProject(@RequestParam(name="name") String name,
                                              @RequestParam(name="profile") String profile,
                                              final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Project prj = projectDao.getByNameAndProfile(name, profile);
        result.setResult(RestUtils.createResult(0, prj, reqid));
        return result;
    }

    @RequestMapping(value="{prjId}", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public DeferredResult<String> getProjectById(@PathVariable(name="prjId") long prjId,
                                  final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Project prj = projectDao.findOne(prjId);
        result.setResult(RestUtils.createResult(0, prj, reqid));
        return result;
    }

    @RequestMapping(value="{prjId}/configs", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public DeferredResult<String> listConfigs(@PathVariable(name="prjId") long prjId,
                                              final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Iterable<Config> configs = configDao.findByProjectId(prjId);
        result.setResult(RestUtils.createResult(0, configs, reqid));
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> createProject(@RequestBody Project project, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        long prjId = configerService.createProject(project);
        result.setResult(RestUtils.createResult(0, prjId, reqid));
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