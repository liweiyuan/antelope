package com.learn.netty.action.param;

import java.util.HashMap;

/**
 * @Author :lwy
 * @Date : 2018/10/17 14:49
 * @Description :
 */
public class ParamMap extends HashMap<String, Object> implements Param {
    @Override
    public String getString(String param) {
        return get(param).toString();
    }

    @Override
    public Integer getInteger(String param) {
        return Integer.parseInt(getString(param));
    }

    @Override
    public Double getDouble(String param) {
        return Double.parseDouble(getString(param));
    }

    @Override
    public Float getFloat(String param) {
        return Float.parseFloat(getString(param));
    }

    @Override
    public Boolean getBoolean(String param) {
        return Boolean.parseBoolean(getString(param));
    }
    
}
