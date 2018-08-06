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
    Config findByProjectIdAndName(long id,String name);

    @Modifying
    @Query("update Config c set c.description = ?2 where c.id = ?1")
    void updateDescription(long id, String description);

    @Query("select c from Config c where c.project.id = ?1 and c.deleted = false")
    List<Config> findByProjectId(long id);

    @Modifying
    @Query("update Config c set c.deleted = true where c.id = ?1")
    void deleteById(long id);

    @Query(nativeQuery = true,
           value = "select c.* from cs_config c " +
                   " inner join cs_config_auth a " +
                   " on c.project_id = ?2 and a.user_id = ?1 and c.id = a.config_id and c.deleted = false")
    List<Config> findByUserIdAndProjectId(long userId, long prjId);
}
