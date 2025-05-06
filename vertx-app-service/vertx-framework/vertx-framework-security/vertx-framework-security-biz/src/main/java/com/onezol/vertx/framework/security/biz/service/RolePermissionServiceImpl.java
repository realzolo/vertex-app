package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertx.framework.security.api.service.RolePermissionService;
import com.onezol.vertx.framework.security.biz.mapper.RolePermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

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

    /**
     * 根据角色ID列表获取角色权限ID列表
     *
     * @param roleIds 角色ID列表
     */
    @Override
    public Map<Long, List<Long>> getPermissionIdsByRoleIds(List<Long> roleIds) {
        roleIds = roleIds.stream().distinct().toList();

        List<RolePermissionEntity> entities = this.list(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .select(RolePermissionEntity::getRoleId, RolePermissionEntity::getPermissionId)
                        .in(!roleIds.isEmpty(), RolePermissionEntity::getRoleId, roleIds)
        );

        return entities.stream().collect(
                Collectors.groupingBy(
                        RolePermissionEntity::getRoleId,
                        Collectors.mapping(RolePermissionEntity::getPermissionId, Collectors.toList())
                )
        );
    }
}
