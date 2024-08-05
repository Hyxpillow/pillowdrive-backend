package com.hyxpillow.pillowdrive.common.interceptor;


import com.hyxpillow.pillowdrive.common.exception.PillowException;
import com.hyxpillow.pillowdrive.common.result.ResultCodeEnum;
import com.hyxpillow.pillowdrive.common.threadlocal.AuthThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws PillowException {
        if ("OPTIONS".equals(request.getMethod())){
            return true;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new PillowException(ResultCodeEnum.LOGIN_COOKIE_FAIL);
        }

        String sessionId = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("sessionid")) {
                sessionId = cookie.getValue();
            }
        }

        // 如果cookies存在且其中不包含sessionid
        if (sessionId == null) {
            throw new PillowException(ResultCodeEnum.LOGIN_COOKIE_FAIL);
        }

        String userInfo = redisTemplate.opsForValue().get("login:"+sessionId);
        if (userInfo == null) {
            throw new PillowException(ResultCodeEnum.LOGIN_COOKIE_FAIL);
        }
        // 把用户id存入thread local
        AuthThreadLocal.set(userInfo);
        return true;
    }
}
