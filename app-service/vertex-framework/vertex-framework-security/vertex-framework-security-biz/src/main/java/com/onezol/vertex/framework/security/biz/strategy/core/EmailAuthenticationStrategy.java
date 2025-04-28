package com.onezol.vertex.framework.security.biz.strategy.core;

import com.onezol.vertex.framework.common.exception.InvalidParameterException;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.security.api.enumeration.LoginType;
import com.onezol.vertex.framework.security.api.model.LoginUserDetails;
import com.onezol.vertex.framework.security.api.model.dto.AuthIdentity;
import com.onezol.vertex.framework.security.biz.authentication.token.EmailAuthenticationToken;
import com.onezol.vertex.framework.security.biz.strategy.LoginStrategy;
import com.onezol.vertex.framework.security.biz.strategy.LoginSupport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class EmailAuthenticationStrategy implements LoginStrategy {

    private final AuthenticationManager authenticationManager;
    private final LoginSupport loginSupport;

    public EmailAuthenticationStrategy(AuthenticationManager authenticationManager, LoginSupport loginSupport) {
        this.authenticationManager = authenticationManager;
        this.loginSupport = loginSupport;
    }

    @Override
    public AuthIdentity login(Object... params) throws AuthenticationException {
        if (params.length < 2) {
            throw new InvalidParameterException("执行登录失败，参数异常。请检查参数列表：email, verificationCode");
        }

        String email = (String) params[0];
        String verificationCode = (String) params[1];
        if (StringUtils.isBlank(email)) throw new InvalidParameterException("邮箱不能为空");
        if (StringUtils.isBlank(verificationCode)) throw new InvalidParameterException("验证码不能为空");

        // 执行认证
        Authentication authentication = createAuthentication(params);
        authentication = authenticationManager.authenticate(authentication);

        // 获取用户信息
        Object principal = authentication.getPrincipal();
        LoginUserDetails loginUserDetails = (LoginUserDetails) principal;

        // 处理登录成功后的逻辑
        return loginSupport.afterLoginSuccess(loginUserDetails, LoginType.UP);
    }

    @Override
    public Authentication createAuthentication(Object... params) {
        String email = (String) params[0];
        String verificationCode = (String) params[1];
        return new EmailAuthenticationToken(email, verificationCode);
    }

}
