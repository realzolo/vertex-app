package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserRegistrationPayload;
import com.onezol.vertex.framework.security.api.model.vo.UserAuthenticationVO;

import java.util.Map;

public interface UserAuthService extends BaseService<UserEntity> {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserEntity getUserByUsername(String username);

    /**
     * 用户注册
     *
     * @param payload UserRegistrationPayload
     */
    UserAuthenticationVO register(UserRegistrationPayload payload);

    /**
     * 用户登录(根据用户名)
     *
     * @param username   用户名
     * @param password   密码
     * @param captcha 验证码
     */
    UserAuthenticationVO loginByIdPassword(String username, String password, String captcha);


    /**
     * 用户登录(根据邮箱)
     *
     * @param email      电子邮箱
     * @param verifyCode 验证码
     */
    UserAuthenticationVO loginByEmail(String email, String verifyCode);

    /**
     * 用户登出
     */
    void logout();
}
