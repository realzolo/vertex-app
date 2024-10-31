package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.security.api.mapper.RoleMapper;
import com.onezol.vertex.framework.security.api.mapper.UserMapper;
import com.onezol.vertex.framework.security.api.mapper.UserRoleMapper;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserRoleEntity;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    public UserRoleServiceImpl(RoleMapper roleMapper, UserMapper userMapper) {
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
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

    /**
     * 解绑用户角色
     *
     * @param userId 用户ID
     */
    @Override
    public void unbindUserRole(Long userId) {
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("userId must not be null");
        }
        UserEntity userEntity = userMapper.selectById(userId);
        if (Objects.isNull(userEntity)) {
            throw new IllegalArgumentException("user not found");
        }
        roleMapper.removeUserRole(userId);
    }
}
