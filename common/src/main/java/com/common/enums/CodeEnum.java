package com.common.enums;

public enum CodeEnum implements BaseErrorInfo {
    // 数据操作错误定义
    SUCCESS("200", "成功！"),
    BODY_NOT_MATCH("400","请求的数据格式不符！"),
    SIGNATURE_NOT_MATCH("401","请求的数字签名不匹配！"),
    NOT_FOUND("404", "未找到该资源！"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误！"),
    SERVER_BUSY("503","服务器正忙，请稍后再试！"),

    // 用户非正常
    USER_AUTH_DENY("4000", "用户权限不足"),
    USER_PASSWORD_ERROR("4001", "用户密码错误"),
    USER_ACCOUNT_EXPIRED("4002", "用户账号过期"),
    USER_PASSWORD_EXPIRED("4003", "用户密码过期"),
    USER_ACCOUNT_DISABLE("4004", "用户账号不可用"),
    USER_ACCOUNT_LOCKED("4005", "用户账号锁定"),
    USER_ACCOUNT_NOT_EXIST("4006", "用户不存在"),
    USER_NOT_LOGIN("4007", "用户未登录");

    /** 错误码 */
    private String resultCode;

    /** 错误描述 */
    private String resultMsg;

    CodeEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
