package com.learn.netty;

import com.learn.AntelopeServer;
import com.learn.netty.annotation.AntelopeAction;

/**
 * @Author :lwy
 * @Date : 2018/10/15 15:37
 * @Description :
 */
@AntelopeAction(value = "hello")
public class AntelopeServerTestCase {


    public static void main(String[] args) throws InterruptedException {

        AntelopeServer.start(AntelopeServerTestCase.class,"/");
    }
}
