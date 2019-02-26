package net.arksea.config;

import akka.routing.ConsistentHashingRouter;

import java.io.Serializable;

/**
 *
 * Created by xiaohaixing on 2017/8/5.
 */

public class ConfigKey implements ConsistentHashingRouter.ConsistentHashable,Serializable {
    private static final long serialVersionUID = -2597538113359207514L;
    public final String project;
    public final String profile;
    public final String config;
    public final String strValue;

    public ConfigKey(final String project,final String profile,final String config) {
        this.project = project;
        this.profile = profile;
        this.config = config;
        strValue = project+":"+profile+":"+config;
    }

    public String toString() {
        return strValue;
    }

    public int hashCode() {
        return strValue.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this.strValue.equals(((ConfigKey) o).strValue);
    }

    @Override
    public Object consistentHashKey() {
        return strValue;
    }
}

