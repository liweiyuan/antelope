package com.learn.netty.util;

import com.learn.netty.context.AntelopeContext;

/**
 * @Author :lwy
 * @Date : 2018/10/15 16:06
 * @Description :
 */
public class ThreadLocalHolder {


    private static final ThreadLocal<Long> LOCAL_TIME = new ThreadLocal<>();

    private static final ThreadLocal<AntelopeContext> ANTELOPE_CONTEXT = new ThreadLocal<>();

    public static void setContext(AntelopeContext context) {
        ANTELOPE_CONTEXT.set(context);
    }

    public static AntelopeContext getContext() {
        return ANTELOPE_CONTEXT.get();
    }

    public static void removeContext() {
        ANTELOPE_CONTEXT.remove();
    }

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
