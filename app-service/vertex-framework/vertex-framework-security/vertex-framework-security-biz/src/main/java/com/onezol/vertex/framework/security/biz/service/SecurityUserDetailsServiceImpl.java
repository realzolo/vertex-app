package com.onezol.vertex.framework.security.biz.service;

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

import java.util.Collections;
import java.util.Set;

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
        RoleEntity role = userRoleService.getUserRole(user.getDetails().getId());
        if (role != null) {
            user.setRoles(Collections.singleton(role.getCode()));

            String permString = permissionService.getRolePermissions(role.getId());
            String[] permissions = permString.split(",");
            user.setPermissions(Set.of(permissions));
        }

        return user;
    }

}
