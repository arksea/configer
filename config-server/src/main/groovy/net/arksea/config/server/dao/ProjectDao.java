package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Project;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/7/17.
 */

public interface ProjectDao extends Repository<Project,Long> {
    List<Project> getAll();
    Project getByName(String name);
    void save(Project project);
}
