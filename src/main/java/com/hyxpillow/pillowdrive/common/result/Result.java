package com.hyxpillow.pillowdrive.common.result;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    private Result() {}

    public static <T> Result<T> ok() {
        Result<T> result = new Result<T>();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<T>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }
    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<T>();
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
}
