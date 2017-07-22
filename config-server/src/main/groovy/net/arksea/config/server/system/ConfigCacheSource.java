package net.arksea.config.server.system;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import net.arksea.acache.IDataSource;
import net.arksea.acache.TimedData;
import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ProjectDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.Project;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;

/**
 *
 * Created by Administrator on 2017/7/22.
 */
@Component
public class ConfigCacheSource implements IDataSource<String,String> {
    @Autowired
    ActorSystem system;
    @Autowired
    ProjectDao projectDao;
    @Autowired
    ConfigDao configDao;

    @Override
    public Future<TimedData<String>> request(String key) {
        return Futures.future(() -> {
            String[] strs = StringUtils.split(key,":", 2);
            if (strs.length == 2) {
                Project prj = projectDao.getByName(strs[0]);
                Config cfg = configDao.getByNameAndProject(strs[1], prj);
                return new TimedData<>(0, cfg.getDoc().getValue());
            } else {
                throw new RuntimeException("Key格式错误: "+key);
            }
        },system.dispatcher());
    }
}
