package com.learn.netty.exception;

import com.learn.netty.enums.StatusType;

/**
 * @Author :lwy
 * @Date : 2018/10/17 11:45
 * @Description :
 */
public class AntelopeException extends CommonException {

    public AntelopeException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public AntelopeException(StatusType requestError, String message) {
        super(message);
        this.errorCode = requestError.getCode();
        this.errorMessage = message;
    }
}
