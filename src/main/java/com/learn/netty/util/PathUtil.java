package com.learn.netty.util;

/**
 * @Author :lwy
 * @Date : 2018/10/17 11:12
 * @Description :
 */
public class PathUtil {


    /**
     * 获取根path
     * @param path
     * @return
     */
    public static String getRootPath(String path){
        return "/"+path.split("/")[0];
    }

    /**
     * 获取Action path
     * @param path
     * @return
     */
    public static String getActionPath(String path){
        return "/"+path.split("/")[1];
    }
}
