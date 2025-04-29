package com.onezol.vertx.framework.security.biz.service;

import com.onezol.vertx.framework.common.util.ValidationUtils;
import com.onezol.vertx.framework.security.api.model.LoginUserDetails;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.dto.UserPassword;
import com.onezol.vertx.framework.security.api.service.UserAuthService;
import com.onezol.vertx.framework.security.api.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoService userInfoService;
    private final UserAuthService userAuthService;

    public SecurityUserDetailsServiceImpl(@Lazy UserInfoService userInfoService, @Lazy UserAuthService userAuthService) {
        this.userInfoService = userInfoService;
        this.userAuthService = userAuthService;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param principal 用户名/邮箱/手机号/...
     * @return 用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        User user;
        if (ValidationUtils.validateEmail(principal)) {
            user = userInfoService.getUserByEmail(principal);
        } else {
            user = userInfoService.getUserByUsername(principal);
        }

        if (user == null) {
            return null;
        }

        UserPassword userPassword = userAuthService.getPassword(user.getId());

        return new LoginUserDetails(user, userPassword);
    }

}
