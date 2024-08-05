package com.hyxpillow.pillowdrive.common.webconfig;

import com.hyxpillow.pillowdrive.common.interceptor.AuthInterceptor;
import com.hyxpillow.pillowdrive.common.interceptor.IPIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private IPIntercepter ipIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/user/get_code",
                        "/user/reset_password");
        registry.addInterceptor(ipIntercepter)
                .addPathPatterns("/**");
    }
}
