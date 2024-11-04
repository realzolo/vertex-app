package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.exception.RuntimeBizException;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.security.api.mapper.RoleMapper;
import com.onezol.vertex.framework.security.api.model.dto.Role;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import com.onezol.vertex.framework.security.api.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    private final RolePermissionService rolePermissionService;

    public RoleServiceImpl(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    /**
     * @param role
     * @return
     */
    @Override
    public void updateRole(Role role) {
        RoleEntity entity = BeanUtils.toBean(role, RoleEntity.class);
        if (!updateById(entity)) {
            throw new RuntimeBizException("更新角色失败");
        }
        // 查询当前角色原权限ID列表
        List<Long> oldPermissionIds = rolePermissionService.list(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .select(RolePermissionEntity::getPermissionId)
                        .eq(RolePermissionEntity::getRoleId, role.getId())
        ).stream().map(RolePermissionEntity::getPermissionId).toList();

        // 当前角色权限列表
        Set<Long> newPermissionIds = role.getPermissionIds();

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
    }
}
