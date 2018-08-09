package net.arksea.config.server.rest;

import net.arksea.config.server.ResultCode;
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
import java.util.Optional;

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
    private ConfigerService configerService;


    @RequestMapping(method = RequestMethod.GET, produces = MEDIA_TYPE)
    public DeferredResult<String> listProjects(final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        Iterable<Project> projects = configerService.listProjects(userId);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, projects, reqid));
        return result;
    }

    @RequestMapping(value="{prjId}", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public DeferredResult<String> getProjectById(@PathVariable(name="prjId") long prjId,
                                  final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        Optional<Project> op = configerService.getProject(userId, prjId);
        if (op.isPresent()) {
            result.setResult(RestUtils.createResult(ResultCode.SUCCEED, op.get(), reqid));
        } else {
            result.setResult(RestUtils.createError(ResultCode.NOT_EXISTS, "未找到指定项目", reqid));
        }
        return result;
    }

    @RequestMapping(value="{prjId}/configs", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public DeferredResult<String> listConfigs(@PathVariable(name="prjId") long prjId,
                                              final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        Iterable<Config> configs = configerService.listProjectConfigs(userId, prjId);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, configs, reqid));
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> createProject(@RequestBody Project project, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        long prjId = configerService.createProject(project, userId);
        result.setResult(RestUtils.createResult(0, prjId, reqid));
        return result;
    }

    @RequestMapping(value="/{projectId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public DeferredResult<String> deleteProject(@PathVariable(name="projectId") long projectId, final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        configerService.deleteProject(userId, projectId);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, "succeed", reqid));
        return result;
    }

    @RequestMapping(value="{prjId}/users", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public DeferredResult<String> getProjectUsers(@PathVariable(name="prjId") long prjId,
                                              final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long userId = (Long)httpRequest.getAttribute("jwt-user-id");
        List<ProjectUser> auths = configerService.getProjectUsers(userId, prjId);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, auths, reqid));
        return result;
    }

    @RequestMapping(value="{prjId}/users/{userId}", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public DeferredResult<String> updateProjectUsers(@PathVariable(name="prjId") long prjId,
                                                     @PathVariable(name="userId") long userId,
                                                     @RequestBody ProjectUser projectUser,
                                                  final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long loginedUserId = (Long)httpRequest.getAttribute("jwt-user-id");
        configerService.updateProjectUser(loginedUserId, prjId, projectUser);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, "succeed", reqid));
        return result;
    }

    @RequestMapping(value="{prjId}/users", method = RequestMethod.POST, produces = MEDIA_TYPE)
    public DeferredResult<String> addProjectUser(@PathVariable(name="prjId") long prjId,
                                                 @RequestBody ProjectUser projectUser,
                                                 final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long loginedUserId = (Long)httpRequest.getAttribute("jwt-user-id");
        configerService.addProjectUser(loginedUserId, prjId, projectUser);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, "succeed", reqid));
        return result;
    }

    @RequestMapping(value="{prjId}/users/{userId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public DeferredResult<String> delProjectUser(@PathVariable(name="prjId") long prjId,
                                                  @PathVariable(name="userId") long userId,
                                                  final HttpServletRequest httpRequest) {
        DeferredResult<String> result = new DeferredResult<>();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        Long loginedUserId = (Long)httpRequest.getAttribute("jwt-user-id");
        configerService.delProjectUser(loginedUserId, prjId, userId);
        result.setResult(RestUtils.createResult(ResultCode.SUCCEED, "succeed", reqid));
        return result;
    }

}
