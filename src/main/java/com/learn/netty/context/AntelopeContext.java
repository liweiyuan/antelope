package com.learn.netty.context;

import com.alibaba.fastjson.JSON;
import com.learn.netty.action.req.AntelopeRequest;
import com.learn.netty.action.res.AntelopeResponse;
import com.learn.netty.constant.AntelopeConstant;
import com.learn.netty.util.ThreadLocalHolder;

/**
 * @Author :lwy
 * @Date : 2018/10/17 15:46
 * @Description :上下文
 */
public class AntelopeContext {


    private AntelopeRequest request;

    private AntelopeResponse response;

    public AntelopeContext(AntelopeRequest request, AntelopeResponse response) {
        this.request = request;
        this.response = response;
    }

    public void json(String text) {
        getResponse().setContentType(AntelopeConstant.ContentType.JSON);
        getResponse().setHttpContent(JSON.toJSONString(text));
    }

    public void text(String text) {
        getResponse().setContentType(AntelopeConstant.ContentType.TEXT);
        getResponse().setHttpContent(text);
    }

    public void html(String text) {
        getResponse().setContentType(AntelopeConstant.ContentType.HTML);
        getResponse().setHttpContent(text);
    }

    public static AntelopeRequest getRequest() {
        return AntelopeContext.getContext().request;
    }

    public static AntelopeResponse getResponse() {
        return AntelopeContext.getContext().response;
    }

    public static void setContext(AntelopeContext context) {
        ThreadLocalHolder.setContext(context);
    }

    public static void removeContext() {
        ThreadLocalHolder.removeContext();
    }

    public static AntelopeContext getContext() {
        return ThreadLocalHolder.getContext();
    }


    public static void main(String[] args) {
        System.out.println(JSON.toJSONString("hello?a=1,2,3&b=a"));
    }
}
