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
        //TODO 1.资源初始化
        AntelopeSetting.setting(clzz,scanRootPath);
        //TODO 2.启动netty服务器
        NettyServer.start();
    }
}
