package com.onezol.vertx.framework.security.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.skeleton.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.UserEntitySoft;
import com.onezol.vertx.framework.security.api.model.payload.UserQueryPayload;
import com.onezol.vertx.framework.security.api.model.payload.UserSavePayload;

import java.util.List;

public interface UserInfoService extends BaseService<UserEntitySoft> {

    /**
     * 根据用户ID取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(long userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据用户邮箱获取用户信息
     *
     * @param email 用户邮箱
     * @return 用户信息
     */
    User getUserByEmail(String email);

    /**
     * 根据用户ID列表取用户信息列表
     * @param userIds 用户ID列表
     * @return 用户信息列表
     */
    List<User> getUsersInfo(List<Long> userIds);

    /**
     * 创建/更新用户
     *
     * @param payload 用户参数
     */
    void createOrUpdateUser(UserSavePayload payload);

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
    PagePack<User> getUserPage(Page<UserEntitySoft> page, UserQueryPayload payload);

    /**
     * 获取未绑定角色的用户列表
     */
    PagePack<User> getUnboundRoleUserPage(Page<UserEntitySoft> page, UserQueryPayload payload);

}
