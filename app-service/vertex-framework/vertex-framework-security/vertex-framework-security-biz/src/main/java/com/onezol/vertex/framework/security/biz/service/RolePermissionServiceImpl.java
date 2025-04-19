package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.Asserts;
import com.onezol.vertex.framework.security.biz.mapper.RolePermissionMapper;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

    /**
     * 根据角色ID删除角色权限关联
     *
     * @param roleId 角色ID
     */
    @Override
    public void removePermissionsByRole(Long roleId) {
        Asserts.notNull(roleId, "角色ID不能为空");

        this.baseMapper.removePermissionsByRole(roleId);
    }

}
