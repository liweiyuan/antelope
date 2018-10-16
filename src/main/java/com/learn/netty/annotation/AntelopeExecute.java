package com.learn.netty.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AntelopeExecute {

    String value() default "" ;
}
