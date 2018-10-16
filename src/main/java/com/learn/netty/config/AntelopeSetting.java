package com.learn.netty.config;

import com.learn.netty.util.ClassScanner;
import com.learn.netty.util.ThreadLocalHolder;

import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/15 16:05
 * @Description :
 */
public class AntelopeSetting {

    /**
     * 资源初始化
     *
     * @param clzz
     * @param scanRootPath
     */
    public static void setting(Class<?> clzz, String scanRootPath) {
        initializeConfig(clzz);
    }

    /**
     * 初始化系统参数
     *
     * @param clzz
     */
    private static void initializeConfig(Class<?> clzz) {
        //初始化开始时间
        ThreadLocalHolder.setLocalTime(System.currentTimeMillis());
        //设置扫描包路径
        AppConfig.newInstance().setRootPackageName(clzz.getPackage().getName());
        //扫描Class
        List<Class<?>> configurations=ClassScanner.configurations(AppConfig.newInstance().getRootPackageName());
        //初始化基于注解的action与拦截器处理
        ClassScanner.initAnnotation();
    }
}
