package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.Asserts;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertx.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertx.framework.security.api.model.entity.UserEntity;
import com.onezol.vertx.framework.security.api.model.entity.UserRoleEntity;
import com.onezol.vertx.framework.security.api.service.RolePermissionService;
import com.onezol.vertx.framework.security.api.service.RoleService;
import com.onezol.vertx.framework.security.api.service.UserInfoService;
import com.onezol.vertx.framework.security.api.service.UserRoleService;
import com.onezol.vertx.framework.security.biz.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    private final RoleService roleService;

    private final UserInfoService userInfoService;

    private final RolePermissionService rolePermissionService;

    public UserRoleServiceImpl(@Lazy RoleService roleService, @Lazy UserInfoService userInfoService, RolePermissionService rolePermissionService) {
        this.roleService = roleService;
        this.userInfoService = userInfoService;
        this.rolePermissionService = rolePermissionService;
    }


    /**
     * 获取用户的角色
     *
     * @param userId 用户ID
     */
    @Override
    public List<RoleEntity> getUserRoles(Long userId) {
        if (userId == null) {
            return null;
        }
        return this.baseMapper.queryUserRoles(userId);
    }

    /**
     * 更新用户角色
     *
     * @param roleId  角色ID
     * @param userIds 用户ID列表
     */
    @Override
    @Transactional
    public void assignToUsers(Long roleId, List<Long> userIds) {
        if (Objects.isNull(roleId) || Objects.isNull(userIds) || userIds.isEmpty()) {
            throw new InvalidParameterException("角色ID或用户ID列表不能为空");
        }

        // 角色校验
        RoleEntity role = roleService.getById(roleId);
        if (Objects.isNull(role)) {
            throw new InvalidParameterException("角色不存在");
        }

        // 用户校验
        long count = userInfoService.count(
                Wrappers.<UserEntity>lambdaQuery()
                        .in(UserEntity::getId, userIds)
        );
        if (count != userIds.size()) {
            throw new InvalidParameterException("部分用户不存在, 无法绑定角色");
        }

        // 角色绑定
        // 查询已存在的关联关系
        List<UserRoleEntity> relations = this.list(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .select(UserRoleEntity::getId, UserRoleEntity::getUserId, UserRoleEntity::getRoleId)
                        .eq(UserRoleEntity::getRoleId, roleId)
        );
        List<Long> shouldSkipUserIds = relations.stream().map(UserRoleEntity::getUserId).toList();
        List<UserRoleEntity> shouldCreateEntities = new ArrayList<>();
        for (Long userId : userIds) {
            if (shouldSkipUserIds.contains(userId)) {
                continue;
            }
            UserRoleEntity entity = new UserRoleEntity();
            entity.setUserId(userId);
            entity.setRoleId(roleId);
            shouldCreateEntities.add(entity);
        }
        this.saveBatch(shouldCreateEntities);
    }

    /**
     * 解绑用户所有角色
     *
     * @param userId 用户ID
     */
    @Override
    public void unbindUserAllRoles(Long userId) {
        this.remove(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .eq(UserRoleEntity::getUserId, userId)
        );
    }

    /**
     * 更新用户角色
     *
     * @param userId    用户ID
     * @param roleCodes 角色编码列表
     */
    @Override
    @Transactional
    public void updateUserRoles(Long userId, List<String> roleCodes) {
        if (Objects.isNull(userId) || Objects.isNull(roleCodes) || roleCodes.isEmpty()) {
            throw new InvalidParameterException("用户ID或用户角色编码列表不能为空");
        }

        // 用户校验
        UserEntity user = userInfoService.getById(userId);
        if (Objects.isNull(user)) {
            throw new InvalidParameterException("用户不存在");
        }

        // 角色校验
        List<RoleEntity> roleEntities = roleService.list(
                Wrappers.<RoleEntity>lambdaQuery()
                        .in(RoleEntity::getCode, roleCodes)
        );
        if (roleEntities.size() != roleCodes.size()) {
            throw new InvalidParameterException("部分角色不存在, 无法绑定用户");
        }

        // 删除旧绑定关系
        this.remove(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .eq(UserRoleEntity::getUserId, userId)
        );

        // 创建新绑定关系
        List<UserRoleEntity> shouldCreateEntities = new ArrayList<>();
        for (String roleCode : roleCodes) {
            Long roleId = roleEntities.stream().filter(role -> role.getCode().equals(roleCode)).findFirst().get().getId();
            UserRoleEntity entity = new UserRoleEntity();
            entity.setUserId(userId);
            entity.setRoleId(roleId);
            shouldCreateEntities.add(entity);
        }
        this.saveBatch(shouldCreateEntities);
    }

    /**
     * 获取角色下的用户列表
     *
     * @param page   分页对象
     * @param roleId 角色ID
     * @return 用户列表
     */
    @Override
    public PagePack<User> getRoleUsers(Page<UserRoleEntity> page, Long roleId) {
        page = this.page(
                page,
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .select(UserRoleEntity::getUserId)
                        .eq(UserRoleEntity::getRoleId, roleId)
        );
        List<UserRoleEntity> records = page.getRecords();
        List<Long> userIds = records.stream().map(UserRoleEntity::getUserId).toList();

        if (userIds.isEmpty()) {
            return PagePack.empty();
        }

        List<User> users = userInfoService.getUsersInfo(userIds);

        return PagePack.of(users, page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 解绑用户角色
     *
     * @param roleId  角色ID
     * @param userIds 用户ID列表
     */
    @Override
    @Transactional
    public void unbindUserRole(Long roleId, List<Long> userIds) {
        Asserts.notNull(roleId, "角色ID不能为空");
        Asserts.notEmpty(userIds, "用户ID不能为空");

        this.remove(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .eq(UserRoleEntity::getRoleId, roleId)
                        .in(UserRoleEntity::getUserId, userIds)
        );
    }

    /**
     * 角色绑定权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     */
    @Override
    @Transactional
    public void bindRolePermissions(Long roleId, List<Long> permissionIds) {
        if (Objects.isNull(roleId) || Objects.isNull(permissionIds) || permissionIds.isEmpty()) {
            throw new InvalidParameterException("角色ID或权限ID列表不能为空");
        }

        Set<Long> permissionIdsSet = new HashSet<>(permissionIds);

        // 获取已存在的关联关系
        List<RolePermissionEntity> rolePermissionEntities = rolePermissionService.list(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .select(RolePermissionEntity::getId, RolePermissionEntity::getPermissionId)
                        .eq(RolePermissionEntity::getRoleId, roleId)
        );

        // 计算需要删除的关联关系 与 需要创建的关联关系
        List<Long> willDeleteRelationIds = new ArrayList<>(permissionIds.size() >> 1);
        for (RolePermissionEntity rolePermissionEntity : rolePermissionEntities) {
            if (!permissionIdsSet.contains(rolePermissionEntity.getPermissionId())) {
                willDeleteRelationIds.add(rolePermissionEntity.getId());
            } else {
                permissionIdsSet.remove(rolePermissionEntity.getPermissionId());
            }
        }
        List<Long> willCreateRelationPermissionIds = permissionIdsSet.stream().toList();

        // 删除被解除的关联关系
        if (!willDeleteRelationIds.isEmpty()) {
            rolePermissionService.removeByIds(willDeleteRelationIds);
        }

        // 创建新的关联关系
        List<RolePermissionEntity> willCreateEntities = new ArrayList<>();
        for (Long permissionId : willCreateRelationPermissionIds) {
            RolePermissionEntity entity = new RolePermissionEntity();
            entity.setRoleId(roleId);
            entity.setPermissionId(permissionId);
            willCreateEntities.add(entity);
        }
        rolePermissionService.saveBatch(willCreateEntities);
    }
}
