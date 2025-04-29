package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.mvc.service.BaseService;
import com.onezol.vertx.framework.security.api.enumeration.LoginType;
import com.onezol.vertx.framework.security.api.model.AuthIdentity;
import com.onezol.vertx.framework.security.api.model.dto.UserPassword;
import com.onezol.vertx.framework.security.api.model.entity.UserEntity;
import com.onezol.vertx.framework.security.api.model.payload.UserSavePayload;

public interface UserAuthService extends BaseService<UserEntity> {

    /**
     * 用户注册
     *
     * @param payload UserRegistrationPayload
     */
    AuthIdentity register(UserSavePayload payload);

    /**
     * 用户登录
     *
     * @param loginType 登录类型
     * @param params    登录参数
     * @return 认证身份信息
     */
    AuthIdentity login(LoginType loginType, Object... params);

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
