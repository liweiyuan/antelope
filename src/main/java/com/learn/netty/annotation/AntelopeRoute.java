package com.learn.netty.annotation;

import java.lang.annotation.*;

/**
 * @Author :lwy
 * @Date : 2018/11/19 15:39
 * @Description :
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AntelopeRoute {
    String value() default "" ;
}
