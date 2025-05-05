package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.constant.enumeration.DisEnableStatus;
import com.onezol.vertx.framework.common.model.TreeNode;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.common.util.TreeUtils;
import com.onezol.vertx.framework.security.api.enumeration.PermissionTypeEnum;
import com.onezol.vertx.framework.security.api.model.dto.Permission;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertx.framework.security.api.service.PermissionService;
import com.onezol.vertx.framework.security.api.service.RolePermissionService;
import com.onezol.vertx.framework.security.api.service.RoleService;
import com.onezol.vertx.framework.security.biz.mapper.PermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    private final RolePermissionService rolePermissionService;
    private final RoleService roleService;

    public PermissionServiceImpl(RolePermissionService rolePermissionService, @Lazy RoleService roleService) {
        this.rolePermissionService = rolePermissionService;
        this.roleService = roleService;
    }

    /**
     * 获取权限树
     *
     * @return 返回权限树
     */
    @Override
    public List<TreeNode> tree() {
        List<Permission> permissions = this.listEnabledPermissions();
        List<TreeNode> nodes = toTreeNodes(permissions);
        return TreeUtils.buildTree(nodes, 0L);
    }

    /**
     * 根据角色 ID 列表获取用户权限标识符集合。
     *
     * @param roleIds 角色 ID 列表
     * @return 返回一个包含权限标识符的集合
     */
    @Override
    public List<String> getRolePermissionKeys(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<PermissionEntity> permissions = this.baseMapper.queryRolePermissions(roleIds);
        List<String> permissionKeys = new ArrayList<>();
        for (PermissionEntity permission : permissions) {
            if (!StringUtils.isBlank(permission.getPermission())) {
                permissionKeys.add(permission.getPermission());
            }
        }
        return permissionKeys;
    }

    /**
     * 根据角色 ID 列表获取权限 ID 集合。
     *
     * @param roleIds 角色 ID 列表
     * @return 返回一个包含权限 ID 的集合
     */
    @Override
    public List<Long> getRolePermissionIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<PermissionEntity> permissions = this.baseMapper.queryRolePermissions(roleIds);
        List<Long> permissionIds = new ArrayList<>();
        for (PermissionEntity permission : permissions) {
            permissionIds.add(permission.getId());
        }
        return permissionIds;
    }

    /**
     * 根据权限 ID 删除权限。
     *
     * @param id 要删除的权限的 ID。
     * @return 如果删除成功返回 true，否则返回 false。
     */
    @Override
    @Transactional
    public boolean deletePermission(Long id) {
        this.removeById(id);
        rolePermissionService.removePermissionByPermissionId(id);
        return true;
    }

    /**
     * 检查指定用户是否拥有任意一个指定的权限。
     *
     * @param userId         用户 ID
     * @param permissionKeys 权限标识符数组
     * @return 如果用户拥有任意一个指定的权限返回 true，否则返回 false。
     */
    @Override
    public boolean hasAnyPermissions(Long userId, String... permissionKeys) {
        // 如果为空，说明已经有权限
        if (permissionKeys == null || permissionKeys.length == 0) {
            return true;
        }

        // 获得当前登录的角色。如果为空，说明没有权限
        List<Role> roles = roleService.getUserRoles(userId);
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }

        // 情况一：获取当前用户所有角色的权限，遍历权限，判断是否包含
        List<Long> roleIds = roles.stream().map(Role::getId).toList();
        List<String> userPermissionKeys = this.getRolePermissionKeys(roleIds);
        for (String permissionKey : permissionKeys) {
            if (userPermissionKeys.contains(permissionKey)) {
                return true;
            }
        }

        // 情况二：超管直接放行(超管拥有所有权限)
        return roleService.hasAnySuperAdmin(userId);
    }

    /**
     * 根据权限类型和状态获取权限列表。
     *
     * @param permissionIds 权限 ID 列表
     */
    @Override
    public List<Permission> listEnabledButtons(List<Long> permissionIds) {
        List<PermissionEntity> permissions = this.list(
                Wrappers.<PermissionEntity>lambdaQuery()
                        .eq(PermissionEntity::getStatus, DisEnableStatus.ENABLE)
                        .ne(PermissionEntity::getType, PermissionTypeEnum.BUTTON)
                        .in(!permissionIds.isEmpty(), PermissionEntity::getId, permissionIds)
        );
        return BeanUtils.toList(permissions, Permission.class);
    }

    private List<Permission> listEnabledPermissions() {
        List<PermissionEntity> permissions = this.list(
                Wrappers.<PermissionEntity>lambdaQuery().eq(PermissionEntity::getStatus, DisEnableStatus.ENABLE)
        );
        return BeanUtils.toList(permissions, Permission.class);
    }

    private List<TreeNode> toTreeNodes(List<Permission> permissions) {
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Permission permission : permissions) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(permission.getId());
            treeNode.setParentId(permission.getParentId());
            treeNode.setTitle(permission.getTitle());
            treeNode.setIcon(permission.getIcon());
            treeNode.setDisabled(false);
            permission.setType(permission.getType());
            permission.setStatus(permission.getStatus());
            treeNode.setData(permission);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

}
