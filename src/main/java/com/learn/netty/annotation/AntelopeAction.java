package com.learn.netty.annotation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AntelopeAction {

    String value() default "" ;
}
