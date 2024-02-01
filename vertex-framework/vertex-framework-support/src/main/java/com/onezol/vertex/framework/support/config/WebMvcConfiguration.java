package com.onezol.vertex.framework.support.config;

import com.onezol.vertex.framework.security.api.interceptor.AuthenticationContextInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(new AuthenticationContextInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/register", "/auth/login");
    }

}
