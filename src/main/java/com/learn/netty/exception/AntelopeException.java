package com.learn.netty.exception;

/**
 * @Author :lwy
 * @Date : 2018/10/17 11:45
 * @Description :
 */
public class AntelopeException extends CommonException {

    public AntelopeException(String message) {
        super(message);
        this.errorMessage=message;
    }

}
