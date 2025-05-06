package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.exception.RuntimeServiceException;
import com.onezol.vertx.framework.common.model.DataPairRecord;
import com.onezol.vertx.framework.common.model.TreeNode;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.TreeUtils;
import com.onezol.vertx.framework.security.api.context.UserIdentityContext;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.security.api.model.dto.Permission;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertx.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertx.framework.security.api.service.PermissionService;
import com.onezol.vertx.framework.security.api.service.RolePermissionService;
import com.onezol.vertx.framework.security.api.service.RoleService;
import com.onezol.vertx.framework.security.api.service.UserRoleService;
import com.onezol.vertx.framework.security.biz.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    private final UserRoleService userRoleService;
    private final PermissionService permissionService;
    private final RolePermissionService rolePermissionService;

    public RoleServiceImpl(UserRoleService userRoleService, PermissionService permissionService, RolePermissionService rolePermissionService) {
        this.userRoleService = userRoleService;
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
    }

    /**
     * 获取角色树形结构。
     *
     * @return 角色树形结构
     */
    @Override
    public List<TreeNode> tree() {
        UserIdentity userIdentity = UserIdentityContext.get();
        List<DataPairRecord> roles = userIdentity.getRoles();
        List<Long> roleIds = roles.stream().map(DataPairRecord::getId).toList();
        List<Long> permissionIds = permissionService.getRolePermissionIds(roleIds);
        List<Permission> permissions = permissionService.listEnabledButtons(permissionIds);
        List<TreeNode> nodes = new ArrayList<>(permissions.size());
        for (Permission permission : permissions) {
            TreeNode node = new TreeNode();
            node.setId(permission.getId());
            node.setParentId(permission.getParentId());
            node.setTitle(permission.getTitle());
            node.setIcon(permission.getIcon());
            node.setDisabled(false);
            node.setData(permission);
            nodes.add(node);
        }
        return TreeUtils.buildTree(nodes, 0L);
    }

    /**
     * @param role 角色
     */
    @Override
    public Role updateRole(Role role) {
        RoleEntity entity = BeanUtils.toBean(role, RoleEntity.class);
        if (!updateById(entity)) {
            throw new RuntimeServiceException("更新角色失败");
        }
        // 查询当前角色原权限ID列表
        List<Long> oldPermissionIds = rolePermissionService.list(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .select(RolePermissionEntity::getPermissionId)
                        .eq(RolePermissionEntity::getRoleId, role.getId())
        ).stream().map(RolePermissionEntity::getPermissionId).toList();

        // 当前角色权限列表
        List<Long> newPermissionIds = role.getPermissionIds();

        // 计算待删除的权限ID列表
        List<Long> removePermissionIds = new ArrayList<>(oldPermissionIds);
        removePermissionIds.removeAll(newPermissionIds);

        // 计算待新增的权限ID列表
        List<Long> addPermissionIds = new ArrayList<>(newPermissionIds);
        addPermissionIds.removeAll(oldPermissionIds);

        // 删除多余的角色权限
        if (!removePermissionIds.isEmpty()) {
            rolePermissionService.remove(
                    Wrappers.<RolePermissionEntity>lambdaQuery()
                            .eq(RolePermissionEntity::getRoleId, role.getId())
                            .in(RolePermissionEntity::getPermissionId, removePermissionIds)
            );
        }

        // 新增角色权限
        if (!addPermissionIds.isEmpty()) {
            List<RolePermissionEntity> rolePermissions = new ArrayList<>();
            for (Long permissionId : addPermissionIds) {
                RolePermissionEntity rolePermission = new RolePermissionEntity();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(permissionId);
                rolePermissions.add(rolePermission);
            }
            rolePermissionService.saveBatch(rolePermissions);
        }
        log.info("更新角色 '{}' 权限完成", role.getName());

        return role;
    }

    /**
     * 获取用户角色列表
     *
     * @param userId 用户ID
     */
    @Override
    public List<Role> getUserRoles(Long userId) {
        return userRoleService.getUserRoles(userId);
    }

    /**
     * 判断用户是否是超级管理员
     *
     * @param userId 用户ID
     */
    @Override
    public boolean hasAnySuperAdmin(Long userId) {
        return userRoleService.hasAnySuperAdmin(userId);
    }

    /**
     * 判断用户是否拥有指定角色（任一一个即可）
     *
     * @param userId    用户ID
     * @param roleCodes 角色编码列表
     */
    @Override
    public boolean hasAnyRoles(Long userId, String... roleCodes) {
        return userRoleService.hasAnyRoles(userId, roleCodes);
    }
}
