package com.onezol.vertex.framework.security.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.DictionaryEntry;
import com.onezol.vertex.framework.common.model.PagePack;
import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserQueryPayload;
import com.onezol.vertex.framework.security.api.model.payload.UserSavePayload;

import java.util.List;
import java.util.Set;

public interface UserInfoService extends BaseService<UserEntity> {

    /**
     * 根据用户ID取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserInfo(long userId);

    /**
     * 根据用户ID列表取用户信息列表
     * @param userIds 用户ID列表
     * @return 用户信息列表
     */
    List<User> getUsersInfo(List<Long> userIds);

    /**
     * 修改用户信息
     */
    User updateUserInfo(UserSavePayload payload);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);

    /**
     * 获取用户字典
     */
    List<DictionaryEntry> getUserDict();

    /**
     * 获取用户列表
     */
    PagePack<User> getUserPage(Page<UserEntity> page, UserQueryPayload payload);

    /**
     * 获取未绑定角色的用户列表
     */
    PagePack<User> getUnboundRoleUserPage(Page<UserEntity> page, UserQueryPayload payload);

}
