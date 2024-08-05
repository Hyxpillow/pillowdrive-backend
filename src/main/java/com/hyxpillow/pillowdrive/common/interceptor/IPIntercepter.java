package com.hyxpillow.pillowdrive.common.interceptor;

import com.hyxpillow.pillowdrive.common.exception.PillowException;
import com.hyxpillow.pillowdrive.common.threadlocal.IpThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class IPIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws PillowException {
        String sourceIP = request.getRemoteAddr();
        IpThreadLocal.set(sourceIP);
        return true;
    }
}
