package com.learn.netty.action.res;

import java.util.Map;

/**
 * @Author :lwy
 * @Date : 2018/10/17 10:46
 * @Description :响应抽象接口
 */
public interface AntelopeResponse {

    //headers
    Map<String, String> getHeaders();

    //set content type
    void setContentType(String contentType);

    //get content type
    String getContentType();

    //set http body
    void setHttpContent(String content);

    //get http body
    String getHttpContent();
}
