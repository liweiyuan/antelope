package com.learn.netty.config;

/**
 * @Author :lwy
 * @Date : 2018/10/16 15:46
 * @Description :
 */
public class AppConfig {

    private static AppConfig INSTANCE;


    public AppConfig() {
    }

    /**
     * 单例构造
     *
     * @return
     */
    public static AppConfig newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppConfig();
        }
        return INSTANCE;
    }

    private String rootPackageName;

    private String rootPath;

    //netty服务端监听端口
    private Integer port = 6363;

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
