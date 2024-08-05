package com.hyxpillow.pillowdrive.common.exception;

import com.hyxpillow.pillowdrive.common.result.Result;
import com.hyxpillow.pillowdrive.common.result.ResultCodeEnum;
import com.hyxpillow.pillowdrive.common.threadlocal.AuthThreadLocal;
import com.hyxpillow.pillowdrive.common.threadlocal.IpThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class PillowExceptionHandler {
    @ExceptionHandler(PillowException.class)
    @ResponseBody
    public Result<String> pillowExceptionHandle(PillowException e) {
        log.error("ERROR ip:{} uid:{} msg:{}", IpThreadLocal.get(), AuthThreadLocal.get(), e.getMessage());
        return Result.fail(e.getResultCodeEnum());
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> handler(Exception e) {
        log.error(e.getMessage(), e);
        return Result.fail(ResultCodeEnum.FAIL);
    }
}
