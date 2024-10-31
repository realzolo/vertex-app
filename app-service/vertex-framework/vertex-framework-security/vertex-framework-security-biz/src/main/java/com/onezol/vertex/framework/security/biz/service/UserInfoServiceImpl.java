package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.constant.RedisKey;
import com.onezol.vertex.framework.common.exception.RuntimeBizException;
import com.onezol.vertex.framework.common.model.PlainPage;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.security.api.mapper.UserMapper;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserQueryPayload;
import com.onezol.vertex.framework.security.api.service.UserInfoService;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserInfoService {

    private final UserRoleService userRoleService;

    private final RedisCache redisCache;

    public UserInfoServiceImpl(UserRoleService userRoleService, RedisCache redisCache) {
        this.userRoleService = userRoleService;
        this.redisCache = redisCache;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User getUserInfo(String username) {
        Objects.requireNonNull(username, "username 不可为空");
        UserEntity entity = this.getOne(
                Wrappers.<UserEntity>lambdaQuery()
                        .eq(UserEntity::getUsername, username)
        );
        return BeanUtils.toBean(entity, User.class);
    }

    /**
     * 修改用户信息
     */
    @Override
    public User updateUserInfo(User user) {
        if (Objects.isNull(user) || Objects.isNull(user.getId())) {
            throw new RuntimeBizException("用户信息不可为空");
        }
        UserEntity entity = BeanUtils.toBean(user, UserEntity.class);
        boolean ok = this.updateById(entity);
        if (!ok) {
            throw new RuntimeBizException("修改用户信息失败");
        }
        UserEntity userEntity = this.getById(user.getId());
        return BeanUtils.toBean(userEntity, User.class);
    }

    /**
     * 删除用户
     *
     * @param userId
     */
    @Override
    public void deleteUser(Long userId) {
        if (Objects.isNull(userId)) {
            throw new RuntimeBizException("用户ID不可为空");
        }
        UserEntity user = this.getById(userId);
        if (Objects.isNull(user)) {
            throw new RuntimeBizException("用户不存在");
        }

        // 判断当前用户是否满足删除条件：...
        // ...

        // 解绑用户角色
        userRoleService.unbindUserRole(userId);

        // 删除Redis相关缓存
        // 1. 删除用户Token缓存
        String userTokenRedisKey = RedisKeyHelper.buildRedisKey(RedisKey.USER_TOKEN, String.valueOf(userId));
        redisCache.deleteObject(userTokenRedisKey);
        // 2. 删除用户信息缓存
        String userInfoRedisKey = RedisKeyHelper.buildRedisKey(RedisKey.USER_INFO, String.valueOf(userId));
        redisCache.deleteObject(userInfoRedisKey);
    }

    /**
     * 获取用户列表
     */
    @Override
    public PlainPage<User> getUserPage(Page<UserEntity> page, UserQueryPayload payload) {
        Page<UserEntity> userPage = this.baseMapper.queryUserPage(page, payload);
        return PlainPage.from(userPage, User.class);
    }

}
