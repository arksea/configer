package net.arksea.config.server.dao;

import net.arksea.config.server.entity.ProjectAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/9/15.
 */
public interface ProjectAuthDao extends CrudRepository<ProjectAuth, Long> {

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
                " where a.project_id = ?1 and a.user_id = ?2 and a.query = true"
    )
    int existsQueryByProjectId(long projectId, long userId);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
            " where a.project_id = ?1 and a.user_id = ?2 and a.manage = true"
    )
    int existsManageByProjectId(long projectId, long userId);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
            " where a.project_id = ?1 and a.user_id = ?2 and a.config = true"
    )
    int existsConfigByProjectId(long projectId, long userId);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
                " inner join (cs_config c) " +
                " on c.id = ?1 and a.user_id = ?2 and a.project_id = c.project_id and a.query = true"
    )
    int existsQueryByConfigId(long configId, long userId);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
            " inner join (cs_config c) " +
            " on c.id = ?1 and a.user_id = ?2 and a.project_id = c.project_id and a.manage = true"
    )
    int existsManageByConfigId(long configId, long userId);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
            " inner join (cs_config c) " +
            " on c.id = ?1 and a.user_id = ?2 and a.project_id = c.project_id and a.config = true"
    )
    int existsConfigByConfigId(long configId, long userId);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
            " inner join (cs_config c) " +
            " on c.doc_id = ?1 and a.user_id = ?2 and a.project_id = c.project_id and a.query = true"
    )
    int existsQueryByDocId(long docId, long userId);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
            " inner join (cs_config c) " +
            " on c.doc_id = ?1 and a.user_id = ?2 and a.project_id = c.project_id and a.manage = true"
    )
    int existsManageByDocId(long docId, long userId);

    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_project_auth a " +
            " inner join (cs_config c) " +
            " on c.doc_id = ?1 and a.user_id = ?2 and a.project_id = c.project_id and a.config = true"
    )
    int existsConfigByDocId(long docId, long userId);

    List<ProjectAuth> getByProjectId(long projectId);

    List<ProjectAuth> getByProjectIdAndUserId(long projectId, long userId);
}
