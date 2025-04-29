package com.onezol.vertx.framework.security.biz.config;

import com.onezol.vertx.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertx.framework.security.biz.authentication.provider.EmailAuthenticationProvider;
import com.onezol.vertx.framework.security.biz.interceptor.JwtAuthenticationTokenFilter;
import com.onezol.vertx.framework.security.biz.handler.UserAccessDeniedHandler;
import com.onezol.vertx.framework.security.biz.handler.UserAuthenticationHandler;
import com.onezol.vertx.framework.security.biz.handler.UserLogoutSuccessHandler;
import com.onezol.vertx.framework.support.support.RequestPathHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

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
    private final EmailAuthenticationProvider emailAuthenticationProvider;
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, UserAuthenticationHandler userAuthenticationHandler, UserAccessDeniedHandler userAccessDeniedHandler, UserLogoutSuccessHandler userLogoutSuccessHandler, EmailAuthenticationProvider emailAuthenticationProvider, UserDetailsService userDetailsService) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.userAuthenticationHandler = userAuthenticationHandler;
        this.userAccessDeniedHandler = userAccessDeniedHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
        this.emailAuthenticationProvider = emailAuthenticationProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 获取限制访问路径并转换为 MvcMatchers
        Set<String> restrictPathSet = RequestPathHelper.getControllerPaths(RestrictAccess.class);
        Set<String> mvcMatchers = RequestPathHelper.convertPathToMvcMatcher(restrictPathSet);
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
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        authenticationManagerBuilder.authenticationProvider(emailAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
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