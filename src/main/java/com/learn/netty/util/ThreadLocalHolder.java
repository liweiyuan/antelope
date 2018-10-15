package com.learn.netty.util;

/**
 * @Author :lwy
 * @Date : 2018/10/15 16:06
 * @Description :
 */
public class ThreadLocalHolder {


    private static final ThreadLocal<Long> LOCAL_TIME = new ThreadLocal<>();


    /**
     * 设置值
     */
    public static void setLocalTime(long time) {
        LOCAL_TIME.set(time);
    }

    public static long getLocalTime() {
        long time = LOCAL_TIME.get();
        LOCAL_TIME.remove();
        return time;
    }


}
