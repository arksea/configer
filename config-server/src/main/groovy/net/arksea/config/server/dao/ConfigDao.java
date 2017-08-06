package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Modifying
    @Query("update Config c set c.description = ?2 where c.id = ?1")
    void updateDescription(long id, String description);
}
