package com.learn.netty;

import com.learn.netty.action.param.Param;
import com.learn.netty.annotation.AntelopeInterceptor;
import com.learn.netty.inteceptor.AbstractAntelopeInterceptorAdapter;

/**
 * @Author :lwy
 * @Date : 2018/11/6 18:17
 * @Description :
 */
@AntelopeInterceptor(order = 3,value = "hello")
public class ExampleInterceptor extends AbstractAntelopeInterceptorAdapter {

    @Override
    public void before(Param param) {
        System.out.println("3333333333333333333 before");
    }

    @Override
    public void after(Param param) {
        System.out.println("3333333333333333333 after");
    }
}
