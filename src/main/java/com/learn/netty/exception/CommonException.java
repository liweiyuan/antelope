package com.learn.netty.exception;

import java.io.Serializable;

/**
 * @Author :lwy
 * @Date : 2018/10/17 11:18
 * @Description :
 */
public class CommonException extends RuntimeException implements Serializable {

    protected String errorMessage;
    protected String errorCode;

    public CommonException() {
    }

    CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
