package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.dto.AuthIdentity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserSavePayload;

public interface UserAuthService extends BaseService<UserEntity> {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserEntity getUserByUsername(String username);

    /**
     * 根据用户邮箱获取用户信息
     *
     * @param email 用户邮箱
     * @return 用户信息
     */
    UserEntity getUserByEmail(String email);

    /**
     * 用户注册
     *
     * @param payload UserRegistrationPayload
     */
    AuthIdentity register(UserSavePayload payload);

    /**
     * 用户登录(根据用户名)
     *
     * @param username  用户名
     * @param password  密码
     * @param sessionId 会话ID
     * @param captcha   验证码
     */
    AuthIdentity loginByIdPassword(String username, String password, String sessionId, String captcha);


    /**
     * 用户登录(根据邮箱)
     *
     * @param email      电子邮箱
     * @param verifyCode 验证码
     */
    AuthIdentity loginByEmail(String email, String verifyCode);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 创建/更新用户
     *
     * @param payload 用户参数
     */
    void createOrUpdateUser(UserSavePayload payload);

}
