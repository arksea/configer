package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.ConfigDoc;
import net.arksea.config.server.entity.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

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
}
