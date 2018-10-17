package com.learn.netty.action.res;

import com.learn.netty.constant.AntelopeConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author :lwy
 * @Date : 2018/10/17 10:50
 * @Description :
 */
public class AntelopeHttpResponse implements AntelopeResponse {

    private Map<String, String> headers = new HashMap<>(8);

    private String contentType;

    private String httpContent;

    public static AntelopeResponse initResponse() {
        AntelopeResponse response = new AntelopeHttpResponse();
        response.setContentType(AntelopeConstant.ContentType.JSON);
        return response;
    }


    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setHttpContent(String content) {
        this.httpContent = content;
    }

    @Override
    public String getHttpContent() {
        return this.httpContent;
    }
}
