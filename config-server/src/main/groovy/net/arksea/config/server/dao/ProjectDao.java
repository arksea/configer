package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Project;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * Created by xiaohaixing on 2017/7/17.
 */

public interface ProjectDao extends CrudRepository<Project,Long> {
    Project getByNameAndProfile(String name,String profile);
}
