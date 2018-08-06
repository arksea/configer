package net.arksea.config.server.dao;

import net.arksea.config.server.entity.ProjectAuth;
import net.arksea.config.server.entity.ProjectFunction;
import net.arksea.config.server.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/9/15.
 */
public interface ProjectAuthDao extends CrudRepository<ProjectAuth, Long> {
    //此处使用nativeQuery，是为了几个方法的func都使用ordinal值
    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
                " where a.project_id = ?1 and a.user_id = ?2 and a.function = ?3"
    )
    int existsByProjectIdAndUserIdAndFunction(long projectId, long userId, int func);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
                " inner join (cs_config c) " +
                " on c.id = ?1 and a.user_id = ?2 and a.project_id = c.project_id and a.function = ?3"
    )
    int existsByConfigIdAndUserIdAndFunction(long configId, long userId, int func);


    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
            " inner join (cs_config c) " +
            " on c.doc_id = ?1 and a.user_id = ?2 and a.project_id = c.project_id and a.function = ?3"
    )
    int existsByDocIdAndUserIdAndFunction(long docId, long userId, int func);
}
