package net.arksea.config.server.entity;

import javax.persistence.*;

/**
 *
 * Created by xiaohaixing on 2017/7/16.
 */
@Entity
@Table(name = "cs_config_doc")
public class ConfigDoc extends IdEntity {
    private String value;
    private String metadata;

    @Column(length = 2048, nullable = false)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(length = 8192, nullable = false)
    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
