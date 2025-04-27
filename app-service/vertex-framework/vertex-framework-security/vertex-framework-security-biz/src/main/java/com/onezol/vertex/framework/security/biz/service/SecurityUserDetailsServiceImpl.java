package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.security.api.model.LoginUserDetails;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.dto.UserPassword;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import com.onezol.vertex.framework.security.api.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
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
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userInfoService.getUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        UserPassword userPassword = userAuthService.getPassword(user.getId());

        return new LoginUserDetails(user, userPassword);
    }

}
