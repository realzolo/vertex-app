package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.exception.RuntimeBizException;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.model.PlainPage;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.security.api.mapper.UserMapper;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserQueryPayload;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import com.onezol.vertex.framework.security.api.service.UserInfoService;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.CacheKeyHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserInfoService {

    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;

    private final RedisCache redisCache;

    public UserInfoServiceImpl(UserRoleService userRoleService, RolePermissionService rolePermissionService, PermissionService permissionService, RedisCache redisCache) {
        this.userRoleService = userRoleService;
        this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
        this.redisCache = redisCache;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public User getUserInfo(long userId) {
        UserEntity entity = this.getById(userId);
        if (Objects.isNull(entity)) {
            throw new RuntimeBizException("用户不存在");
        }
        User user = BeanUtils.toBean(entity, User.class);
        // 获取用户角色
        List<RoleEntity> roleEntities = userRoleService.getUserRoles(userId);
        List<LabelValue<String, String>> roles = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            roles.add(new LabelValue<>(roleEntity.getName(), roleEntity.getCode()));
        }
        user.setRoles(roles);
        // 获取用户权限
        Set<String> roleCodes = roleEntities.stream().map(RoleEntity::getCode).collect(Collectors.toSet());
        if (roleCodes.contains("super")) {
            user.setPermissions(List.of("*:*:*"));
        } else {
            List<Long> roleIds = roleEntities.stream().map(RoleEntity::getId).toList();
            Set<String> rolePermissionKeys = permissionService.getRolePermissionKeys(roleIds);
            List<String> permissions = List.copyOf(rolePermissionKeys);
            user.setPermissions(permissions);
        }
        return user;
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
        List<String> roleCodes = user.getRoles().stream().map(LabelValue::getValue).toList();

        // 解绑用户角色
        userRoleService.unbindUserRole(user.getId());
        // 绑定用户角色
        userRoleService.updateUserRoleByRoleCode(user.getId(), roleCodes);

        return this.getUserInfo(user.getId());
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
        String userTokenRedisKey = CacheKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, String.valueOf(userId));
        redisCache.deleteObject(userTokenRedisKey);
        // 2. 删除用户信息缓存
        String userInfoRedisKey = CacheKeyHelper.buildCacheKey(CacheKey.USER_INFO, String.valueOf(userId));
        redisCache.deleteObject(userInfoRedisKey);
    }

    /**
     * 获取用户列表
     */
    @Override
    public PlainPage<User> getUserPage(Page<UserEntity> page, UserQueryPayload payload) {
        Page<UserEntity> userPage = this.baseMapper.queryUserPage(page, payload);
        PlainPage<User> resultPage = PlainPage.from(userPage, User.class);
        resultPage.getItems().forEach(userEntity -> {
            List<RoleEntity> userRoles = userRoleService.getUserRoles(userEntity.getId());
            if (userRoles != null && !userRoles.isEmpty()) {
                List<LabelValue<String, String>> roles = new ArrayList<>();
                for (RoleEntity roleEntity : userRoles) {
                    roles.add(new LabelValue<>(roleEntity.getName(), roleEntity.getCode()));
                }
                userEntity.setRoles(roles);
            }
        });
        return resultPage;
    }

}
