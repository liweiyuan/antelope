package com.learn.netty;

import com.learn.netty.annotation.AntelopeAction;
import com.learn.netty.annotation.AntelopeRoute;

/**
 * @Author :lwy
 * @Date : 2018/11/19 15:58
 * @Description :
 */
@AntelopeAction(value = "route")
public class RouteController {


    @AntelopeRoute("test")
    public String getResult(){
        System.out.println("hello");
        return "hello";
    }


    @AntelopeRoute("hello")
    public String hello(){
        System.out.println("hello");
        return "hello";
    }

}
