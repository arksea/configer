package net.arksea.config;

import java.io.Serializable;

/**
 *
 * Created by xiaohaixing on 2019/2/22.
 */
public class UpdateConfigDoc implements Serializable {
    public final String accessToken;
    public final String project;
    public final String profile;
    public final String config;
    public final String value;

    public UpdateConfigDoc(String accessToken, String project, String profile, String config, String value) {
        this.accessToken = accessToken;
        this.project = project;
        this.profile = profile;
        this.config = config;
        this.value = value;
    }
}
