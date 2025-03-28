package com.onezol.vertex.framework.security.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.PlainPage;
import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserQueryPayload;

public interface UserInfoService extends BaseService<UserEntity> {

    /**
     * 根据用户ID取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserInfo(long userId);


    /**
     * 修改用户信息
     */
    User updateUserInfo(User user);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);


    /**
     * 获取用户列表
     */
    PlainPage<User> getUserPage(Page<UserEntity> page, UserQueryPayload payload);

}
