package com.learn.netty.configuration;

import java.util.Properties;

/**
 * @Author :lwy
 * @Date : 2018/10/16 16:57
 * @Description :
 */
public abstract class AbstractAntelopeConfiguration {

    /**
     * file name
     */
    private String propertiesName;

    private Properties properties;

    public String getPropertiesName() {
        return propertiesName;
    }

    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        return properties.get(key) == null ? null : properties.get(key).toString();
    }
}
