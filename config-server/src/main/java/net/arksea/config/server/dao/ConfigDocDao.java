package net.arksea.config.server.dao;

import net.arksea.config.server.entity.ConfigDoc;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 *
 * Created by xiaohaixing on 2017/7/17.
 */
public interface ConfigDocDao extends Repository<ConfigDoc,Long> {
    void save(ConfigDoc cfg);
    @Modifying
    @Query("update ConfigDoc c set c.value = ?2 where c.id = ?1")
    void updateValue(long id, String value);

    @Modifying
    @Query("update ConfigDoc c set c.metadata = ?2 where c.id = ?1")
    void updateSchema(long id, String schema);

    @Query(
        nativeQuery = true,
        value = "select d.id from cs_project p,cs_config c,cs_config_doc d"
              + " where p.name = ?1 and p.profile = ?2 and c.project_id = p.id"
              + " and c.name = ?3 and c.doc_id = d.id")
    long getIdByName(String projectName, String profile, String configName);
}
