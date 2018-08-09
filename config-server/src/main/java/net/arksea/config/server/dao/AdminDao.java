package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Admin;
import net.arksea.config.server.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/9/15.
 */
public interface AdminDao extends CrudRepository<Admin, Long> {
    boolean existsByUserId(long userId);
}
