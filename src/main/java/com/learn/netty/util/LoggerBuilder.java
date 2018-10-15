package com.learn.netty.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author :lwy
 * @Date : 2018/10/15 15:54
 * @Description :
 */
public class LoggerBuilder {


    /**
     * @param clz class
     * @return
     */
    public static Logger getLogger(Class<?> clz) {
        return LoggerFactory.getLogger(clz);
    }
}
