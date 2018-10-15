package com.learn.netty;

import com.learn.AntelopeServer;

/**
 * @Author :lwy
 * @Date : 2018/10/15 15:37
 * @Description :
 */
public class AntelopServerTestCase {


    public static void main(String[] args) throws InterruptedException {

        AntelopeServer.start(AntelopServerTestCase.class,"/");
    }
}
