package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

    private final UserAuthService userAuthService;
    private final UserRoleService userRoleService;
    private final PermissionService permissionService;

    @Autowired
    public SecurityUserDetailsServiceImpl(@Lazy UserAuthService userAuthService, UserRoleService userRoleService, RolePermissionService rolePermissionService, PermissionService permissionService) {
        this.userAuthService = userAuthService;
        this.userRoleService = userRoleService;
        this.permissionService = permissionService;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userAuthService.getUserByUsername(username);
        if (userEntity == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        LoginUser user = new LoginUser(userEntity);
        List<RoleEntity> roleEntities = userRoleService.getUserRoles(user.getDetails().getId());
        if (roleEntities != null && !roleEntities.isEmpty()) {
            // 获取用户角色
            List<LabelValue<String,String>> roles = new ArrayList<>();
            for (RoleEntity roleEntity : roleEntities) {
                roles.add(LabelValue.of(roleEntity.getCode(), roleEntity.getCode()));
            }
            user.setRoles(roles);
            // 获取用户权限
            List<Long> roleIds = roleEntities.stream().map(RoleEntity::getId).toList();
            Set<String> rolePermissionKeys = permissionService.getRolePermissionKeys(roleIds);
            List<String> permissions = rolePermissionKeys.stream().toList();
            user.setPermissions(permissions);
        }

        return user;
    }

}
