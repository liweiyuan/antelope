package com.learn.netty.action.req;

import io.netty.handler.codec.http.DefaultHttpRequest;

/**
 * @Author :lwy
 * @Date : 2018/10/17 10:39
 * @Description :
 */
public class AntelopeHttpRequest implements AntelopeRequest {

    private String method;
    private String url;

    /**
     * init 自定义的http请求
     * @param msg
     * @return
     */
    public static AntelopeRequest initRequest(DefaultHttpRequest msg) {
        AntelopeRequest request = new AntelopeHttpRequest();
        ((AntelopeHttpRequest) request).method = msg.method().name();
        ((AntelopeHttpRequest) request).url = msg.uri();
        return request;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}
