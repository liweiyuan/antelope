package com.learn.netty.action.param;

/**
 * @Author :lwy
 * @Date : 2018/10/17 14:46
 * @Description :
 */
public interface Param {

    String getString(String param);

    Integer getInteger(String param);

    Double getDouble(String param);

    Float getFloat(String param);

    Boolean getBoolean(String param);

}
