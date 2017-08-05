package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/7/17.
 */
public interface ConfigDao extends CrudRepository<Config,Long> {
    Config getByNameAndProject(String name, Project project);
    List<Config> findByProject(Project project);
    List<Config> findByProjectId(long id);
}
