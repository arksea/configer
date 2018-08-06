package net.arksea.config.server.service;

import net.arksea.config.server.dao.ConfigAuthDao;
import net.arksea.config.server.dao.ProjectAuthDao;
import net.arksea.config.server.entity.ProjectFunction;
import net.arksea.restapi.RestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 鉴权服务
 * Created by xiaohaixing on 2018/8/2.
 */
@Component
public class AuthService {
    private Logger logger = LogManager.getLogger(AuthService.class);
    @Autowired
    ProjectAuthDao projectAuthDao;
    @Autowired
    ConfigAuthDao configAuthDao;
    /**
     * 验证用户是否拥有项目的指定权限
     * @param userId
     * @param prjId
     * @param func
     * @return
     */
    /**
     * 验证用户是否拥有项目的指定权限, 没有权限抛出指定异常
     * @param userId
     * @param prjId
     * @param func
     */
    public void verifyProjectAuth(long userId, long prjId, ProjectFunction func) throws RestException {
        boolean has = hasProjectAuth(userId, prjId, func);
        if (!has) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    public boolean hasProjectAuth(long userId, long prjId, ProjectFunction func) {
        return projectAuthDao.existsByProjectIdAndUserIdAndFunction(prjId, userId, func.ordinal()) == 1
               || func == ProjectFunction.QUERY &&
                  configAuthDao.existsByPrjIdAndUserId(prjId, userId) == 1;
    }

    /**
     * 验证用户是否拥有配置管理权限
     * @param userId
     * @param cfgId
     * @return
     */
    public void verifyManagerByConfigId(long userId, long cfgId) {
        boolean exists = projectAuthDao.existsByConfigIdAndUserIdAndFunction(cfgId, userId, ProjectFunction.MANAGER.ordinal()) == 1;
        if (!exists) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    public void verifyManagerByDocId(long userId, long docId) {
        boolean exists = projectAuthDao.existsByDocIdAndUserIdAndFunction(docId, userId, ProjectFunction.MANAGER.ordinal()) == 1;
        if (!exists) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    public void verifyConfigerByConfigId(long userId, long cfgId) {
        boolean exists = configAuthDao.existsByConfigIdAndUserId(cfgId, userId);
        if (!exists) {
            exists = projectAuthDao.existsByConfigIdAndUserIdAndFunction(cfgId, userId, ProjectFunction.CONFIG.ordinal()) == 1;
            if (!exists) {
                throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
        }
    }

    public void verifyConfigerByDocId(long userId, long docId) {
        boolean exists = configAuthDao.existsByDocIdAndUserId(docId, userId) == 1;
        if (!exists) {
            exists = projectAuthDao.existsByDocIdAndUserIdAndFunction(docId, userId, ProjectFunction.CONFIG.ordinal()) == 1;
            if (!exists) {
                throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
        }
    }
}
