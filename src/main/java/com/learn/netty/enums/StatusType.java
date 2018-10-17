package com.learn.netty.enums;

/**
 * @Author :lwy
 * @Date : 2018/10/17 11:49
 * @Description :
 */
public enum  StatusType {
    /** 成功 */
    SUCCESS("9000", "成功"),
    /** 成功 */
    FALLBACK("8000", "FALL_BACK"),
    /** 参数校验失败**/
    VALIDATION_FAIL("3000", "参数校验失败"),
    /** 失败 */
    FAIL("4000", "失败"),

    /** 重复请求 */
    REPEAT_REQUEST("5000", "重复请求"),

    /** 请求限流 */
    REQUEST_LIMIT("6000", "请求限流"),

    /** 请求限流 */
    REQUEST_ERROR("7000", "Request Error"),
    ;


    /** 枚举值码 */
    private final String code;

    /** 枚举描述 */
    private final String message;

    StatusType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
