package com.hyxpillow.pillowdrive.controller;

import com.hyxpillow.pillowdrive.common.exception.PillowException;
import com.hyxpillow.pillowdrive.common.result.Result;
import com.hyxpillow.pillowdrive.common.threadlocal.IpThreadLocal;
import com.hyxpillow.pillowdrive.dto.request.GetCodeRequest;
import com.hyxpillow.pillowdrive.dto.request.LoginRequest;
import com.hyxpillow.pillowdrive.dto.request.RegisterRequest;
import com.hyxpillow.pillowdrive.dto.request.ResetPasswordRequest;
import com.hyxpillow.pillowdrive.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    public Result<String> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest) throws PillowException {
        // 读取参数
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        // 日志记录
        log.info("LOGIN ip:{} email:{}, password:{}", IpThreadLocal.get(), email, password);
        // 调用service层
        String sessionID = userService.login(email, password);
        // 设置cookie
        Cookie cookie = new Cookie("sessionid", sessionID);
        cookie.setMaxAge(86400 * 30);
        cookie.setPath("/");
        response.addCookie(cookie);
        return Result.ok();
    }
    @PostMapping("/user/register")
    public Result<String> register(@RequestBody RegisterRequest registerRequest) throws PillowException {
        // 读取参数
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String code = registerRequest.getCode();
        // 日志记录
        log.info("REGISTER ip:{} email:{}, password:{}, code:{}", IpThreadLocal.get(), email, password, code);
        // 调用service层
        userService.register(email, password, code);
        return Result.ok();
    }

    @PostMapping("/user/reset_password")
    public Result<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws PillowException {
        // 读取参数
        String email = resetPasswordRequest.getEmail();
        String password = resetPasswordRequest.getPassword();
        String code = resetPasswordRequest.getCode();
        // 日志记录
        log.info("RESET_PASSWORD ip:{} email:{}, password:{}, code:{}", IpThreadLocal.get(), email, password, code);
        // 调用service层
        userService.resetPassword(email, password, code);
        return Result.ok();
    }

    @PostMapping("/user/get_code")
    public Result<String> getCode(@RequestBody GetCodeRequest getCodeRequest) throws PillowException {
        // 读取参数
        String email = getCodeRequest.getEmail();
        // 日志记录
        log.info("GET_CODE ip:{} email:{}", IpThreadLocal.get(), email);
        // 调用service层
        userService.getCode(email);
        return Result.ok();
    }

}
