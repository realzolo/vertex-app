package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.security.api.mapper.RoleMapper;
import com.onezol.vertex.framework.security.api.mapper.UserMapper;
import com.onezol.vertex.framework.security.api.mapper.UserRoleMapper;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserRoleEntity;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(RoleMapper roleMapper, UserMapper userMapper, UserRoleMapper userRoleMapper) {
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * 获取用户的角色
     *
     * @param userId 用户ID
     */
    @Override
    public List<RoleEntity> getUserRoles(Long userId) {
        if (userId == null) {
            return null;
        }
        return roleMapper.queryUserRoles(userId);
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

    /**
     * @param userId 用户ID
     * @param roles  角色列表
     */
    @Override
    @Transactional
    public void updateUserRole(Long userId, List<Long> roleIds) {
        UserEntity userEntity = userMapper.selectById(userId);
        if (Objects.isNull(userEntity)) {
            throw new IllegalArgumentException("user not found");
        }
        if (Objects.isNull(roleIds) || roleIds.isEmpty()) {
            return;
        }
        // 删除用户角色
        roleMapper.removeUserRole(userId);
        // 填加新用户角色
        List<RoleEntity> roleEntities = roleMapper.selectList(Wrappers.<RoleEntity>lambdaQuery().in(RoleEntity::getId, roleIds));
        List<UserRoleEntity> userRoleEntities = new ArrayList<>(roleEntities.size());
        roleEntities.forEach(roleEntity -> {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userId);
            userRoleEntity.setRoleId(roleEntity.getId());
            userRoleEntities.add(userRoleEntity);
        });
        boolean ok = this.saveBatch(userRoleEntities);

        StringBuilder simpleRoleNames = new StringBuilder();
        for (int i = 0; i < roleEntities.size(); i++) {
            if (i < 3) {
                simpleRoleNames.append(roleEntities.get(i).getName()).append(" ");
            } else if (!simpleRoleNames.toString().endsWith("...")){
                simpleRoleNames.append("...");
            }
        }
        if (!ok) {
            String message = String.format("用户 '%s' 绑定角色 '%s' 失败！", userEntity.getName(), simpleRoleNames);
            throw new RuntimeException(message);
        }
        log.info("用户 '{}' 绑定角色 '{}' 完成", userEntity.getName(), simpleRoleNames);
    }


    /**
     * @param userId
     * @param roleCodes
     */
    @Override
    @Transactional
    public void updateUserRoleByRoleCode(Long userId, List<String> roleCodes) {
        UserEntity userEntity = userMapper.selectById(userId);
        if (Objects.isNull(userEntity)) {
            throw new IllegalArgumentException("user not found");
        }
        if (Objects.isNull(roleCodes) || roleCodes.isEmpty()) {
            return;
        }
        List<Long> roleIds = roleMapper.selectList(
                Wrappers.<RoleEntity>lambdaQuery()
                        .select(RoleEntity::getId)
                        .in(RoleEntity::getCode, roleCodes)
        ).stream().map(RoleEntity::getId).toList();

        updateUserRole(userId, roleIds);
    }
}
