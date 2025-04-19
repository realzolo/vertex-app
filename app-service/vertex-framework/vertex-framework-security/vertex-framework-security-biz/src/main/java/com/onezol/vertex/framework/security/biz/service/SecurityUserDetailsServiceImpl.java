package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.security.api.model.LoginUserDetails;
import com.onezol.vertex.framework.security.api.model.dto.Department;
import com.onezol.vertex.framework.security.api.model.dto.SimpleDepartment;
import com.onezol.vertex.framework.security.api.model.dto.SimpleRole;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import com.onezol.vertex.framework.security.api.service.UserDepartmentService;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

    private final UserAuthService userAuthService;
    private final UserDepartmentService userDepartmentService;
    private final UserRoleService userRoleService;
    private final PermissionService permissionService;

    public SecurityUserDetailsServiceImpl(@Lazy UserAuthService userAuthService, UserRoleService userRoleService, UserDepartmentService userDepartmentService, PermissionService permissionService) {
        this.userAuthService = userAuthService;
        this.userRoleService = userRoleService;
        this.userDepartmentService = userDepartmentService;
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

        // 获取用户部门
        SimpleDepartment simpleDepartment = null;
        Department department = userDepartmentService.getUserDepartment(userEntity.getId());
        if (department != null) {
            simpleDepartment = SimpleDepartment.of(department.getId(), department.getName());
        }

        // 获取用户角色
        List<RoleEntity> roleEntities = userRoleService.getUserRoles(userEntity.getId());
        List<SimpleRole> roles = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            SimpleRole role = SimpleRole.of(roleEntity.getId(), roleEntity.getName(), roleEntity.getCode());
            roles.add(role);
        }

        // 获取用户权限
        List<Long> roleIds = roleEntities.stream().map(RoleEntity::getId).toList();
        Set<String> rolePermissionKeys = permissionService.getRolePermissionKeys(roleIds);
        List<String> permissions = rolePermissionKeys.stream().toList();

        return new LoginUserDetails(userEntity, simpleDepartment, roles, permissions);
    }

}
