package com.learn.netty;

import com.learn.AntelopServer;

/**
 * @Author :lwy
 * @Date : 2018/10/15 15:37
 * @Description :
 */
public class AntelopServerTestCase {


    public static void main(String[] args) throws InterruptedException {

        AntelopServer.start(AntelopServerTestCase.class,"/");
    }
}
