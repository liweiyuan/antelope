package com.learn.netty.route;

import com.learn.netty.exception.AntelopeException;
import com.learn.netty.util.ClassScanner;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author :lwy
 * @Date : 2018/11/19 16:10
 * @Description :
 */
public class RouteManager {


    private static RouteManager routeManager=null;

    public static RouteManager instance(){
        if(routeManager==null){
            synchronized (RouteManager.class){
                if(routeManager==null){
                    routeManager=new RouteManager();
                }

            }
        }
        return routeManager;
    }

    public static Method getRouteMethod(QueryStringDecoder queryStringDecoder){
        Map<String,Method> routeMap=ClassScanner.getRouteMap();
        if(routeMap==null){
            ClassScanner.initAnnotation();
        }
        String path = queryStringDecoder.path();

        assert routeMap != null;
        Method method=routeMap.get(path);
        if(method==null){
            return null;
        }
        return method;
    }
}
