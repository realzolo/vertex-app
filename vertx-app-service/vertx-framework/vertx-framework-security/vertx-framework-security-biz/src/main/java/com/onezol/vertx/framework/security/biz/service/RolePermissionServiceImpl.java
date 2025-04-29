package com.onezol.vertx.framework.security.biz.service;

import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertx.framework.security.api.service.RolePermissionService;
import com.onezol.vertx.framework.security.biz.mapper.RolePermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

    /**
     * 根据角色ID删除角色权限关联
     *
     * @param roleId 角色ID
     */
    @Override
    public void removePermissionsByRoleId(Long roleId) {
        if (Objects.isNull(roleId)) {
            throw new InvalidParameterException("角色ID不能为空");
        }

        this.baseMapper.removePermissionsByRoleId(roleId);
    }

    /**
     * 根据权限ID删除角色权限关联
     *
     * @param permissionId 权限ID
     */
    @Override
    public void removePermissionByPermissionId(Long permissionId) {
        if (Objects.isNull(permissionId)) {
            throw new InvalidParameterException("权限ID不能为空");
        }

        this.baseMapper.removePermissionByPermissionId(permissionId);
    }
}
