package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.mvc.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.entity.RoleEntity;

public interface RoleService extends BaseService<RoleEntity> {

    /**
     * 更新角色
     * @param role 角色
     */
    void updateRole(Role role);

}
