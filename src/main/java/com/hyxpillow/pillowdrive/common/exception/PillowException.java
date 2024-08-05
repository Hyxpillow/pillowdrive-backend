package com.hyxpillow.pillowdrive.common.exception;

import com.hyxpillow.pillowdrive.common.result.ResultCodeEnum;
import lombok.Getter;

@Getter
public class PillowException extends Exception {
    private final ResultCodeEnum resultCodeEnum;

    public PillowException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }

    @Override
    public String getMessage() {
        return resultCodeEnum.getMessage();
    }
}
