package com.onezol.vertx.framework.security.biz.strategy;

import com.onezol.vertx.framework.security.api.enumeration.LoginType;
import com.onezol.vertx.framework.security.api.model.AuthIdentity;
import com.onezol.vertx.framework.security.api.model.LoginUserDetails;
import com.onezol.vertx.framework.security.api.service.LoginHistoryService;
import com.onezol.vertx.framework.security.api.service.LoginUserService;
import com.onezol.vertx.framework.support.support.JWTHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginSupport {

    private final LoginUserService loginUserService;
    private final LoginHistoryService loginHistoryService;

    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    public LoginSupport(LoginUserService loginUserService, LoginHistoryService loginHistoryService) {
        this.loginUserService = loginUserService;
        this.loginHistoryService = loginHistoryService;
    }

    /**
     * 登录成功后的处理
     *
     * @param loginUserDetails 登录用户身份信息
     * @return 登录成功后的处理结果
     */
    public AuthIdentity afterLoginSuccess(LoginUserDetails loginUserDetails, final LoginType loginType) {
        // 生成token
        String token = JWTHelper.generateToken(loginUserDetails.getId().toString());

        // 缓存用户数据
        loginUserService.addLoginUser(loginUserDetails, token);

        // 存储登录日志
        loginHistoryService.createLoginRecord(loginUserDetails, loginType);

        // 返回结果
        return AuthIdentity.builder()
                .user(loginUserDetails)
                .jwt(
                        AuthIdentity.Ticket.builder()
                                .token(token)
                                .expire(Long.valueOf(expirationTime))
                                .build()
                )
                .build();
    }
}
