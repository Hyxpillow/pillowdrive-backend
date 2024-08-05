package com.hyxpillow.pillowdrive.common.webconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Content-Type", "Content-Disposition")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("*").maxAge(600);
    }
}
