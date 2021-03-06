package com.learn.netty;

import com.learn.netty.action.param.Param;
import com.learn.netty.annotation.AntelopeInterceptor;
import com.learn.netty.inteceptor.AbstractAntelopeInterceptorAdapter;

/**
 * @Author :lwy
 * @Date : 2018/10/16 18:03
 * @Description :
 */
@AntelopeInterceptor(order = 1,value = "test")
public class AntelopeInteceptorTestCase extends AbstractAntelopeInterceptorAdapter {

    @Override
    public void before(Param param) {
        System.err.println("hello before  1");
    }

    @Override
    public void after(Param param) {
        System.err.println("hello after   1");
    }
}
