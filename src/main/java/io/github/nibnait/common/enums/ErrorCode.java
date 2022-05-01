package io.github.nibnait.common.enums;

/**
 * Created by nibnait on 2022/04/03
 */
public enum ErrorCode implements AbstractErrorCode {

    SUCCESS(0L, "success"),

    /**
     * 错误码规则：
     * 第1位 8代表不透传给端上，7代表透传；
     * 第2位 0通用模块 1 lottery， 2 lottery-c，3 lottery-b
     * 01 代表大模块下的子模块
     * 0001 为内部的错误码，如数据库错误
     */
    SERVICE_ERROR(80000001L, "service error"),
    NOT_LOGIN(80000002L, "用户未登录"),
    LOGIN_EXPIRED(80000003L, "登录已过期，请重新登录"),

    ;

    private Long code;
    private String message;

    @Override
    public Long getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    ErrorCode() {
    }

    ErrorCode(Long code, String message) {
        this.code = code;
        this.message = message;
    }
}
