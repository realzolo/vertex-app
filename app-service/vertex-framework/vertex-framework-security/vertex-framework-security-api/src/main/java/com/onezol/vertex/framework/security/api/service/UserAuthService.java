package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.dto.AuthIdentity;
import com.onezol.vertex.framework.security.api.model.dto.UserPassword;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserSavePayload;

public interface UserAuthService extends BaseService<UserEntity> {

    /**
     * 用户注册
     *
     * @param payload UserRegistrationPayload
     */
    AuthIdentity register(UserSavePayload payload);

    /**
     * 用户登录(用户名密码登录)
     *
     * @param username         用户名
     * @param password         密码
     * @param fingerprint      用户指纹
     * @param verificationCode 验证码
     */
    AuthIdentity loginByIdPassword(String username, String password, String fingerprint, String verificationCode);


    /**
     * 用户登录(根据邮箱)
     *
     * @param email            电子邮箱
     * @param verificationCode 验证码
     */
    AuthIdentity loginByEmail(String email, String verificationCode);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取用户密码信息
     *
     * @param userId 用户ID
     */
    UserPassword getPassword(Long userId);

}
