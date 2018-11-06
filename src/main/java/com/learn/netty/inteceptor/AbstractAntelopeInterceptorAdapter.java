package com.learn.netty.inteceptor;

import com.learn.netty.action.param.Param;

import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/17 15:01
 * @Description :
 */
public abstract class AbstractAntelopeInterceptorAdapter implements AntelopeInterceptor {

    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    @Override
    public void before(Param param) {

    }

    @Override
    public void after(Param param) {

    }
}
