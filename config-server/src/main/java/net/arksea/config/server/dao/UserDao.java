package net.arksea.config.server.dao;

import net.arksea.config.server.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * Created by xiaohaixing on 2017/9/15.
 */
public interface UserDao extends CrudRepository<User, Long> {
    boolean existsByName(String name);
    List<User> findByName(String name);
}
