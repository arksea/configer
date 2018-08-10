package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/7/17.
 */
@Repository
public interface ProjectDao extends CrudRepository<Project,Long> {
    Project getByNameAndProfile(String name,String profile);

    @Modifying
    @Query("update Project p set p.deleted = true where p.id = ?1")
    void deleteById(long id);

    @Query(
        nativeQuery=true,
        value=
            "select distinct u.* from (" +
                "  select p.* from cs_project p " +
                "          inner join cs_project_auth a" +
                "          on a.user_id = ?1 and a.query = true and p.id = a.project_id and p.deleted = false" +
                "  union" +
                "  select p.* from cs_project p" +
                "         inner join (cs_config c, cs_config_auth ac)" +
                "         on ac.user_id = ?1 and ac.config_id = c.id and p.id = c.project_id and p.deleted = false" +
                ") u"
    )
    List<Project> findByUserId(long userId);
}
