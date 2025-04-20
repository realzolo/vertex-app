package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.constant.enumeration.SystemRoleType;
import com.onezol.vertex.framework.common.exception.RuntimeServiceException;
import com.onezol.vertex.framework.common.model.PageModel;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.security.biz.mapper.UserMapper;
import com.onezol.vertex.framework.security.api.model.dto.Department;
import com.onezol.vertex.framework.security.api.model.dto.SimpleDepartment;
import com.onezol.vertex.framework.security.api.model.dto.SimpleRole;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserQueryPayload;
import com.onezol.vertex.framework.security.api.model.payload.UserSavePayload;
import com.onezol.vertex.framework.security.api.service.*;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserInfoService {

    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;

    private final RedisCache redisCache;
    private final UserDepartmentService userDepartmentService;

    public UserInfoServiceImpl(UserRoleService userRoleService, RolePermissionService rolePermissionService, PermissionService permissionService, RedisCache redisCache, UserDepartmentService userDepartmentService) {
        this.userRoleService = userRoleService;
        this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
        this.redisCache = redisCache;
        this.userDepartmentService = userDepartmentService;
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
            throw new RuntimeServiceException("用户不存在");
        }
        User user = BeanUtils.toBean(entity, User.class);
        // 获取用户角色
        List<RoleEntity> roleEntities = userRoleService.getUserRoles(userId);
        List<SimpleRole> roles = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            roles.add(SimpleRole.of(roleEntity.getId(), roleEntity.getName(), roleEntity.getCode()));
        }
        user.setRoles(roles);
        // 获取用户权限
        Set<String> roleCodes = roleEntities.stream().map(RoleEntity::getCode).collect(Collectors.toSet());
        if (roleCodes.contains(SystemRoleType.SUPER.getValue()) || roleCodes.contains(SystemRoleType.ADMIN.getValue())) {
            user.setPermissions(List.of("*:*:*"));
        } else {
            List<Long> roleIds = roleEntities.stream().map(RoleEntity::getId).toList();
            Set<String> rolePermissionKeys = permissionService.getRolePermissionKeys(roleIds);
            List<String> permissions = List.copyOf(rolePermissionKeys);
            user.setPermissions(permissions);
        }

        Department department = userDepartmentService.getUserDepartment(userId);
        if (department != null) {
            user.setDepartment(SimpleDepartment.of(department.getId(), department.getName()));
        }
        return user;
    }

    /**
     * 根据用户ID列表取用户信息列表
     *
     * @param userIds 用户ID列表
     * @return 用户信息列表
     */
    @Override
    public List<User> getUsersInfo(List<Long> userIds) {
        userIds = userIds.stream().distinct().toList();

        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            User user = this.getUserInfo(userId);
            users.add(user);
        }
        return users;
    }

    /**
     * 修改用户信息
     */
    @Override
    public User updateUserInfo(UserSavePayload payload) {
        if (Objects.isNull(payload) || Objects.isNull(payload.getId())) {
            throw new RuntimeServiceException("用户信息不可为空");
        }
        UserEntity entity = BeanUtils.toBean(payload, UserEntity.class);
        boolean ok = this.updateById(entity);
        if (!ok) {
            throw new RuntimeServiceException("修改用户信息失败");
        }
        List<String> roleCodes = payload.getRoleCodes();

        // 解绑用户角色
        userRoleService.unbindUserAllRoles(payload.getId());
        // 绑定用户角色
        userRoleService.updateUserRoles(payload.getId(), roleCodes);

        return this.getUserInfo(payload.getId());
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    @Override
    public void deleteUser(Long userId) {
        if (Objects.isNull(userId)) {
            throw new RuntimeServiceException("用户ID不可为空");
        }
        UserEntity user = this.getById(userId);
        if (Objects.isNull(user)) {
            throw new RuntimeServiceException("用户不存在");
        }

        // 判断当前用户是否满足删除条件：...
        // ...

        // 解绑用户角色
        userRoleService.unbindUserAllRoles(userId);

        // 删除Redis相关缓存
        // 1. 删除用户Token缓存
        String userTokenRedisKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, String.valueOf(userId));
        redisCache.deleteObject(userTokenRedisKey);
        // 2. 删除用户信息缓存
        String userInfoRedisKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, String.valueOf(userId));
        redisCache.deleteObject(userInfoRedisKey);
    }

    /**
     * 获取用户列表
     */
    @Override
    public PageModel<User> getUserPage(Page<UserEntity> page, UserQueryPayload payload) {
        Page<UserEntity> userPage = this.baseMapper.queryUserPage(page, payload);
        PageModel<User> resultPage = PageModel.from(userPage, User.class);
        Collection<User> users = resultPage.getItems();

        for (User user : users) {
            List<RoleEntity> userRoles = userRoleService.getUserRoles(user.getId());
            if (userRoles != null && !userRoles.isEmpty()) {
                List<SimpleRole> roles = new ArrayList<>();
                for (RoleEntity roleEntity : userRoles) {
                    roles.add(SimpleRole.of(roleEntity.getId(), roleEntity.getName(), roleEntity.getCode()));
                }
                user.setRoles(roles);
            }
            Department department = userDepartmentService.getUserDepartment(user.getId());
            if (department != null) {
                user.setDepartment(SimpleDepartment.of(department.getId(), department.getName()));
            }
        }

        return resultPage;
    }

    /**
     * 获取未绑定角色的用户列表
     */
    @Override
    public PageModel<User> getUnboundRoleUserPage(Page<UserEntity> page, UserQueryPayload payload) {
        Page<UserEntity> userPage = this.baseMapper.queryUnboundRoleUserPage(page, payload);
        PageModel<User> resultPage = PageModel.from(userPage, User.class);
        Collection<User> users = resultPage.getItems();

        for (User user : users) {
            List<RoleEntity> userRoles = userRoleService.getUserRoles(user.getId());
            if (userRoles != null && !userRoles.isEmpty()) {
                List<SimpleRole> roles = new ArrayList<>();
                for (RoleEntity roleEntity : userRoles) {
                    roles.add(SimpleRole.of(roleEntity.getId(), roleEntity.getName(), roleEntity.getCode()));
                }
                user.setRoles(roles);
            }
            Department department = userDepartmentService.getUserDepartment(user.getId());
            if (department != null) {
                user.setDepartment(SimpleDepartment.of(department.getId(), department.getName()));
            }
        }

        return resultPage;
    }

}
