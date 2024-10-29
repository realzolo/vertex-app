package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.security.api.mapper.RoleMapper;
import com.onezol.vertex.framework.security.api.mapper.UserRoleMapper;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserRoleEntity;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    private final RoleMapper roleMapper;

    public UserRoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * 获取用户的角色
     *
     * @param userId 用户ID
     */
    @Override
    public RoleEntity getUserRole(Long userId) {
        if (userId == null) {
            return null;
        }
        return roleMapper.queryUserRole(userId);
    }

}
