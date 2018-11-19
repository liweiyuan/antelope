package com.learn.netty.route;

import com.learn.netty.util.LoggerBuilder;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author :lwy
 * @Date : 2018/11/19 16:23
 * @Description :
 */
public class RouteProcess {

    private static Logger logger = LoggerBuilder.getLogger(RouteProcess.class);

    private static RouteProcess instance;

    public static RouteProcess newInstance() {
        if (instance == null) {
            synchronized (RouteProcess.class) {
                if (instance == null) {
                    instance = new RouteProcess();
                }
            }
        }
        return instance;
    }

    /**
     * 调用的方法执行
     *
     * @param method
     * @param queryStringDecoder
     */
    public void invoke(Method method, QueryStringDecoder queryStringDecoder) {
        //获取参数列表

        try {
            Object[] objects = getMethodParameters(method, queryStringDecoder);
            Object bean = method.getDeclaringClass().newInstance();
            if (objects == null || objects.length < 1) {
                //TODO 增加一个FactoryBean的工具类来实现bean的管理
                method.invoke(bean);
            } else {
                method.invoke(bean, objects);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            logger.error("获取参数失败",e);
        } catch (InvocationTargetException e) {
            logger.error("调用方法失败",e);
        }
    }

    private Object[] getMethodParameters(Method method, QueryStringDecoder queryStringDecoder) throws IllegalAccessException, InstantiationException {

        Class<?>[] parameterType = method.getParameterTypes();


        //TODO
        Object[] objects = new Object[parameterType.length];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = parameterType[i].newInstance();
        }
        return objects;
    }
}
