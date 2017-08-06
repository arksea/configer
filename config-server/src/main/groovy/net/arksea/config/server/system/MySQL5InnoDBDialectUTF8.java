package net.arksea.config.server.system;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 *
 * Created by xiaohaixing on 2017/8/6.
 */

public class MySQL5InnoDBDialectUTF8 extends MySQL5InnoDBDialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}