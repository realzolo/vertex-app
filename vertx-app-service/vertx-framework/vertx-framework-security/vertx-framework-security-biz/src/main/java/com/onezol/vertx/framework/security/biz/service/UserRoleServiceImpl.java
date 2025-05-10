package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.security.api.enumeration.BuiltinRole;
import com.onezol.vertx.framework.security.api.model.dto.Role;
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
     * 根据用户 ID 获取该用户所拥有的角色列表。
     *
     * @param userId 用户的唯一标识符，用于指定要查询角色的用户。
     * @return 返回一个包含用户角色信息的列表，如果用户没有角色则返回空列表。
     */
    @Override
    public List<Role> getUserRoles(Long userId) {
        if (userId == null) {
            return null;
        }
        List<RoleEntity> entities = this.baseMapper.queryUserRoles(userId);
        List<Long> roleIds = entities.stream().map(RoleEntity::getId).toList();
        Map<Long, List<Long>> permissionIdsByRoleIds = rolePermissionService.getPermissionIdsByRoleIds(roleIds);
        return BeanUtils.toList(entities, Role.class);
    }

    /**
     * 将指定角色分配给多个用户。
     *
     * @param roleId  要分配的角色的唯一标识符。
     * @param userIds 要分配角色的用户 ID 列表。
     */
    @Override
    @Transactional
    public void assignRoleToUsers(Long roleId, List<Long> userIds) {
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
     * 解除指定用户与所有角色的分配关系。
     *
     * @param userId 用户的唯一标识符，用于指定要解除角色分配的用户。
     */
    @Override
    public void unassignAllRolesFromUser(Long userId) {
        this.remove(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .eq(UserRoleEntity::getUserId, userId)
        );
    }

    /**
     * 更新指定用户的角色列表。
     * 该方法会先解除用户与所有现有角色的分配关系，然后将新的角色分配给用户。
     *
     * @param userId    用户的唯一标识符，用于指定要更新角色的用户。
     * @param roleCodes 新的角色编码列表，用于指定要分配给用户的角色。
     */
    @Override
    @Transactional
    public void assignRolesForUser(Long userId, List<String> roleCodes) {
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
     * 根据分页信息和角色 ID 获取该角色下的用户列表。
     *
     * @param page   分页对象，用于指定查询的页码和每页记录数。
     * @param roleId 角色的唯一标识符，用于指定要查询用户的角色。
     * @return 返回一个包含用户信息的分页包装对象。
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
     * 解除指定用户与指定角色的分配关系。
     *
     * @param roleId  要解除分配的角色的唯一标识符。
     * @param userIds 要解除角色分配的用户 ID 列表。
     */
    @Override
    @Transactional
    public void unassignRoleFromUsers(Long roleId, List<Long> userIds) {
        // TODO: 默认管理员不可解除
        this.remove(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .eq(UserRoleEntity::getRoleId, roleId)
                        .in(UserRoleEntity::getUserId, userIds)
        );
    }

    /**
     * 为指定角色分配多个权限。
     *
     * @param roleId        要分配权限的角色的唯一标识符。
     * @param permissionIds 要分配给角色的权限 ID 列表。
     */
    @Override
    @Transactional
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
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

    /**
     * 检查指定用户是否为超级管理员。
     *
     * @param userId 用户的唯一标识符，用于指定要检查的用户。
     * @return 如果用户是超级管理员则返回 true，否则返回 false。
     */
    @Override
    public boolean hasAnySuperAdmin(Long userId) {
        List<Role> userRoles = this.getUserRoles(userId);
        if (Objects.isNull(userRoles) || userRoles.isEmpty()) {
            return false;
        }
        for (Role role : userRoles) {
            if (role.getCode().equals(BuiltinRole.SUPER.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查指定用户是否拥有任意一个指定的角色。
     *
     * @param userId    用户的唯一标识符，用于指定要检查的用户。
     * @param roleCodes 角色编码数组，用于指定要检查的角色。
     * @return 如果用户拥有任意一个指定的角色则返回 true，否则返回 false。
     */
    @Override
    public boolean hasAnyRoles(Long userId, String[] roleCodes) {
        if (Objects.isNull(userId) || Objects.isNull(roleCodes) || roleCodes.length == 0) {
            return false;
        }
        List<Role> userRoles = this.getUserRoles(userId);
        return userRoles.stream().anyMatch(role -> Arrays.asList(roleCodes).contains(role.getCode()));
    }

}
