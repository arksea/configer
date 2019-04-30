package net.arksea.config.server.system;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import net.arksea.acache.IDataSource;
import net.arksea.acache.TimedData;
import net.arksea.config.ConfigKey;
import net.arksea.config.server.dao.ConfigDao;
import net.arksea.config.server.dao.ProjectDao;
import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;

/**
 *
 * Created by Administrator on 2017/7/22.
 */
@Component
public class ConfigCacheSource implements IDataSource<ConfigKey,String> {
    @Autowired
    ActorSystem system;
    @Autowired
    ProjectDao projectDao;
    @Autowired
    ConfigDao configDao;

    @Value("${config.cacheMinutes:5}")
    private int CACHE_DEFAULT_MINUTES;

    @Override
    public Future<TimedData<String>> request(ActorRef cacheActor, String cacheName, ConfigKey key) {
        return Futures.future(() -> {
            Project prj = projectDao.getByNameAndProfile(key.project, key.profile);
            Config cfg = configDao.getByNameAndProject(key.config, prj);
            int cacheMinutes = cfg.getCacheMinutes() == 0 ? CACHE_DEFAULT_MINUTES : cfg.getCacheMinutes();
            long expiredTime = System.currentTimeMillis() + cacheMinutes * 60_000;
            return new TimedData<>(expiredTime, cfg.getDoc().getValue());
        },system.dispatcher());
    }
}
