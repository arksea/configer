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

public interface ProjectDao extends CrudRepository<Project,Long> {
    Project getByNameAndProfile(String name,String profile);

    @Modifying
    @Query("update Project p set p.deleted = true where p.id = ?1")
    void deleteById(long id);

    @Query("select p from Project p where p.deleted = false")
    List<Project> getAllNotDeleted();
}
