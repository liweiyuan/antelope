package com.learn.netty.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AntelopeInterceptor {
    String value() default "" ;

    //TODO 后续添加拦截器的输序执行
    //int order() default 0;
}
