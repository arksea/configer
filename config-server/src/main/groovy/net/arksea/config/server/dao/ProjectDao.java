package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Project;
import org.springframework.data.repository.Repository;

/**
 *
 * Created by xiaohaixing on 2017/7/17.
 */

public interface ProjectDao extends Repository<Project,Long> {
    Project getByName(String name);
    void save(Project project);
}
