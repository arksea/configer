package net.arksea.config.server.service;

import net.arksea.config.server.dao.*;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.Project;
import net.arksea.config.server.entity.ProjectAuth;
import net.arksea.config.server.entity.User;
import net.arksea.config.server.rest.ProjectUser;
import net.arksea.restapi.RestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

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
    @Autowired
    private AuthService authService;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProjectAuthDao projectAuthDao;
    /**
     * 新增配置
     * @param cfg
     * @return 返回增加的配置ID，如果同名配置已存在，则返回 -1
     */
    public Config createConfig(long userId, Config cfg) {
        authService.verifyManagerByConfigId(userId, cfg.getProject().getId());
        Config cfg1 = configDao.findByProjectIdAndName(cfg.getProject().getId(), cfg.getName());
        if (cfg1 != null) {
            cfg.setId(cfg1.getId());
            cfg.getDoc().setId(cfg1.getDoc().getId());
        }
        cfg1 = configDao.save(cfg);
        logger.info("create config project={}, profile={}, config={}, userId={}", cfg1.getProject().getName(), cfg1.getProject().getProfile(), cfg1.getName(), userId);
        return cfg1;
    }

    public long createProject(Project prj, long userId) {
        boolean isAdmin = adminDao.existsByUserId(userId);
        if (isAdmin) {
            Project prj1 = projectDao.save(prj);
            logger.info("create project name={}, profile={}, desc={}, userId={}",prj1.getName(),prj1.getProfile(), prj1.getDescription(), userId);
            return prj1.getId();
        } else {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    public void updateConfigDescription(long userId, long cfgId, String desc) {
        authService.verifyManagerByConfigId(userId, cfgId);
        configDao.updateDescription(cfgId, desc);
        logger.info("update config description configId={}, desc={}, userId={}",cfgId,desc,userId);
    }

    public void updateConfigDoc(long userId, long docId, String configDoc) {
        authService.verifyConfigerByDocId(userId, docId);
        configDocDao.updateValue(docId, configDoc);
        logger.info("update config value docId={}, value={}, userId={}",docId, configDoc, userId);
    }

    public void updateConfigSchema(long userId, long docId, String configSchema) {
        authService.verifyManagerByConfigId(userId, docId);
        configDocDao.updateSchema(docId, configSchema);
        logger.info("update config metadata docId={}, userId={}",docId, userId);
    }

    public void deleteConfig(long userId, long cfgId) {
        authService.verifyManagerByConfigId(userId, cfgId);
        configDao.deleteById(cfgId);
        logger.info("delete config, Id={}, userId={}", cfgId, userId);
    }

    public void deleteProject(long userId, long prjId) {
        authService.verifyProjectAuth(userId, prjId, ProjectFunction.MANAGER);
        projectDao.deleteById(prjId);
        logger.info("delete project, prjId={}, userName={}",prjId, userId);
    }

    public Optional<Project> getProject(long userId, long prjId) {
        authService.verifyProjectAuth(userId, prjId, ProjectFunction.QUERY);
        Project prj = projectDao.findOne(prjId);
        return Optional.of(prj);
    }

    public Iterable<Config> listProjectConfigs(long userId, long prjId) {
        boolean has = authService.hasProjectAuth(userId, prjId, ProjectFunction.QUERY);
        if (has) {
            return configDao.findByProjectId(prjId);
        } else {
            return configDao.findByUserIdAndProjectId(userId, prjId);
        }
    }

    public Iterable<Project> listProjects(long userId) {
        return projectDao.findByUserId(userId);
    }

    public List<ProjectUser> getProjectUsers(long userId, long prjId) {
        boolean isAdmin = adminDao.existsByUserId(userId);
        if (!isAdmin) {
            authService.verifyProjectAuth(userId, prjId, ProjectFunction.MANAGER);
        }
        Iterable<ProjectAuth> auths = projectAuthDao.getByProjectId(prjId);
        List<ProjectUser> userList = new LinkedList<>();
        for (ProjectAuth a : auths) {
            Long uid = a.getUser().getId();
            ProjectUser u = new ProjectUser();
            u.setUserId(uid);
            u.setUserName(a.getUser().getName());
            u.setQuery(a.isQuery());
            u.setManage(a.isManage());
            u.setConfig(a.isConfig());
            userList.add(u);
        }
        return userList;
    }

    public void updateProjectUser(long loginedUserId, long prjId, ProjectUser user) {
        boolean isAdmin = adminDao.existsByUserId(loginedUserId);
        if (!isAdmin) {
            authService.verifyProjectAuth(loginedUserId, prjId, ProjectFunction.MANAGER);
        }
        List<ProjectAuth> auths = projectAuthDao.getByProjectIdAndUserId(prjId, user.getUserId());
        ProjectAuth a;
        if (auths.size() > 0) {
            a = auths.get(0);
            a.setQuery(user.isQuery());
            a.setManage(user.isManage());
            a.setConfig(user.isConfig());
        } else {
            a = new ProjectAuth();
            a.setQuery(user.isQuery());
            a.setManage(user.isManage());
            a.setConfig(user.isConfig());
            Project p = new Project();
            p.setId(prjId);
            a.setProject(p);
            User u = new User();
            u.setId(user.getUserId());
            a.setUser(u);
        }
        projectAuthDao.save(a);
    }

    public long addProjectUser(long loginedUserId, long prjId, ProjectUser user) {
        boolean isAdmin = adminDao.existsByUserId(loginedUserId);
        if (!isAdmin) {
            authService.verifyProjectAuth(loginedUserId, prjId, ProjectFunction.MANAGER);
        }
        long userId = user.getUserId();
        if (userId == -1) {
            List<User> users = userDao.findByName(user.getUserName());
            if (users.size() > 0) {
                userId = users.get(0).getId();
            } else {
                throw new RestException(HttpStatus.BAD_REQUEST, "User not exists");
            }
        }
        ProjectAuth a = new ProjectAuth();
        a.setQuery(user.isQuery());
        a.setManage(user.isManage());
        a.setConfig(user.isConfig());
        Project p = new Project();
        p.setId(prjId);
        a.setProject(p);
        User u = new User();
        u.setId(userId);
        a.setUser(u);
        ProjectAuth aSaved = projectAuthDao.save(a);
        return aSaved.getId();
    }

    public void delProjectUser(long loginedUserId, long prjId, long userId) {
        boolean isAdmin = adminDao.existsByUserId(loginedUserId);
        if (!isAdmin) {
            authService.verifyProjectAuth(loginedUserId, prjId, ProjectFunction.MANAGER);
        }
        projectAuthDao.deleteByProjectIdAndUserId(prjId, userId);
    }
}
