package com.onezol.vertex.framework.security.biz.config;

import com.onezol.vertex.framework.common.util.ControllerPathUtils;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.biz.fillter.JwtAuthenticationTokenFilter;
import com.onezol.vertex.framework.security.biz.handler.UserAccessDeniedHandler;
import com.onezol.vertex.framework.security.biz.handler.UserAuthenticationHandler;
import com.onezol.vertex.framework.security.biz.handler.UserLogoutSuccessHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * JWT认证过滤器
     */
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    /**
     * 认证入口点处理器(认证失败处理器)
     */
    private final UserAuthenticationHandler userAuthenticationHandler;
    /**
     * 拒绝访问处理类(权限不足)
     */
    private final UserAccessDeniedHandler userAccessDeniedHandler;

    /**
     * 用户注销处理器
     */
    private final UserLogoutSuccessHandler userLogoutSuccessHandler;

    public SecurityConfig(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, UserAuthenticationHandler userAuthenticationHandler, UserAccessDeniedHandler userAccessDeniedHandler, UserLogoutSuccessHandler userLogoutSuccessHandler) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.userAuthenticationHandler = userAuthenticationHandler;
        this.userAccessDeniedHandler = userAccessDeniedHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 获取限制访问路径并转换为 MvcMatchers
        Set<String> restrictPathSet = ControllerPathUtils.getControllerPaths(RestrictAccess.class);
        Set<String> mvcMatchers = ControllerPathUtils.convertPathToMvcMatcher(restrictPathSet);
        String[] restrictPaths = mvcMatchers.toArray(new String[0]);

        return http
                // 禁用 httpBasic
                .httpBasic(AbstractHttpConfigurer::disable)
                // 禁用 csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用 session
                .sessionManagement(AbstractHttpConfigurer::disable)
                // 禁用 form 表单登录
                .formLogin(AbstractHttpConfigurer::disable)
                //  配置请求权限
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                // 允许所有OPTIONS请求
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                // 限制访问路径
                                .requestMatchers(restrictPaths).authenticated()
                                // 其他请求放行
                                .anyRequest().permitAll()
                )
                // JWT 认证过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 认证失败处理器
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer
                                // 认证入口点处理器
                                .authenticationEntryPoint(userAuthenticationHandler)
                                // 权限不足处理器
                                .accessDeniedHandler(userAccessDeniedHandler)
                )
                // Logout 处理器
                .logout(logoutConfigurer ->
                        logoutConfigurer
                                // 注销路径
                                .logoutUrl("/logout")
                                // 注销成功处理器
                                .logoutSuccessHandler(userLogoutSuccessHandler)
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 密码编码器
     *
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}