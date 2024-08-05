package com.hyxpillow.pillowdrive.common.result;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    LOGIN_COOKIE_FAIL(400, "登录过期"),

    USER_NOT_FOUND(401, "用户名不存在"),
    LOGIN_PASSWD_FAIL(401, "密码错误"),
    USER_FOUND(401, "用户名已存在"),
    EMAIL_FORMAT_FAIL(401, "邮箱不合法"),
    CODE_NOT_EXIST(401, "未填入验证码"),
    CODE_FAIL(401, "验证码错误"),
    PASSWD_TOO_SHORT(401, "密码长度需大于6位"),

    FILE_NOT_FOUND(402, "文件不存在"),
    FILE_FOUND(402, "文件已存在"),

    MAX_FILE_SIZE_EXCEED(403, "上传文件的大小不能超过50M"),

    FAIL(500, "出现了意料之外的错误");

    private final Integer code;
    private final String message;
    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
