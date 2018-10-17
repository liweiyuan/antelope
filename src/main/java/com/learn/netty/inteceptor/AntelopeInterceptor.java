package com.learn.netty.inteceptor;

import com.learn.netty.action.param.Param;

/**
 * @Author :lwy
 * @Date : 2018/10/17 14:59
 * @Description :
 */
public interface AntelopeInterceptor {


    void before(Param param);

    void after(Param param);
}
