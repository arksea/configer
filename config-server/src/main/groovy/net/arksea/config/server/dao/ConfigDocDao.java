package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.ConfigDoc;
import net.arksea.config.server.entity.Project;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/7/17.
 */
public interface ConfigDocDao extends Repository<ConfigDoc,Long> {
    void save(ConfigDoc cfg);
}
