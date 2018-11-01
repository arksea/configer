package net.arksea.config.server.dao;

import net.arksea.config.server.entity.ConfigAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/9/15.
 */
public interface ConfigAuthDao extends CrudRepository<ConfigAuth, Long> {
    boolean existsByConfigIdAndUserId(long configId, long userId);
    @Query(
        nativeQuery = true,
        value = "select count(1)>0 from cs_config_auth a inner join (cs_user u, cs_config c) " +
                " on c.doc_id = ?1 and a.config_id = c.id and u.id = ?2"
    )
    int existsByDocIdAndUserId(long docId, long userId);


    @Query(
        nativeQuery = true,
        value = "select count(1)>0 " +
                " from cs_config_auth a inner join cs_config c " +
                " on c.project_id = ?1 and a.config_id = c.id and a.user_id = ?2"
    )
    int existsByPrjIdAndUserId(long prjId, long userId);

    List<ConfigAuth> getByConfigId(long configId);
}
