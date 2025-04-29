package com.onezol.vertx.framework.support.config;

import com.onezol.vertx.framework.common.util.ReflectionUtils;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 认证上下文拦截器
        Class<?> clazz = ReflectionUtils.getClass("com.onezol.vertx.framework.security.biz.interceptor.AuthenticationContextInterceptor");
        WebRequestInterceptor authenticationContextInterceptor = (WebRequestInterceptor) ReflectionUtils.newInstance(clazz);
        registry.addWebRequestInterceptor(authenticationContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/register", "/auth/login");
    }

    /**
     * 配置错误页面 <br/>
     * 集成React前端项目时，前端路由会导致后端404错误，这里配置404错误页面跳转到index.html
     */
//    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
            Set<ErrorPage> errorPages = new HashSet<>();
            errorPages.add(error404Page);
            factory.setErrorPages(errorPages);
        };
    }

}
