package com.learn;

import com.learn.netty.NettyServer;
import com.learn.netty.config.AntelopeSetting;

/**
 * @Author :lwy
 * @Date : 2018/10/12 18:18
 * @Description :
 */
public class AntelopeServer {


    /**
     * 启动类
     */
    public static void start(Class<?> clzz, String scanRootPath) throws InterruptedException {
        AntelopeSetting.setting(clzz,scanRootPath);
        NettyServer.start();
    }

    /**
     * 启动类
     * @param clzz
     * @throws InterruptedException
     */
    public static void start(Class<?> clzz) throws InterruptedException {
        AntelopeSetting.setting(clzz,"/");
        NettyServer.start();
    }
}
