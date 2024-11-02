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
        rolePermissionService.remove(Wrappers.<RolePermissionEntity>lambdaQuery().eq(RolePermissionEntity::getRoleId, entity.getId()));
        if (role.getPermissionIds() != null && !role.getPermissionIds().isEmpty()) {
            Set<Long> permissionIds = role.getPermissionIds();
            List<RolePermissionEntity> rolePermissions = new ArrayList<>();
            for (Long permissionId : permissionIds) {
                RolePermissionEntity rolePermission = new RolePermissionEntity();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(permissionId);
                rolePermissions.add(rolePermission);
            }
            boolean ok = rolePermissionService.saveBatch(rolePermissions);
            if (!ok) {
                throw new RuntimeBizException("更新角色权限失败");
            }
        }
    }
}
