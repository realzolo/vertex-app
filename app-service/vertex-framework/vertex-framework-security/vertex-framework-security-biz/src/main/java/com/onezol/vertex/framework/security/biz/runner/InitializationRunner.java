package com.onezol.vertex.framework.security.biz.runner;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.constant.enums.UserGender;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserRoleEntity;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import com.onezol.vertex.framework.security.api.service.RoleService;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 初始化
 */
@Slf4j
//@Component
public class InitializationRunner implements ApplicationRunner {

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    private final RolePermissionService rolePermissionService;

    private final UserAuthService userAuthService;

    private final PasswordEncoder passwordEncoder;

    public InitializationRunner(RoleService roleService, UserRoleService userRoleService, RolePermissionService rolePermissionService, UserAuthService userAuthService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.rolePermissionService = rolePermissionService;
        this.userAuthService = userAuthService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        this.initUsers();
        this.initRoles();
        this.initUserRole();
        this.initRolePermissions();
    }

    /**
     * 初始化角色
     */
    private void initRoles() {
        this.createRole("超级管理员", "super");
        this.createRole("系统管理员", "admin");
    }

    /**
     * 初始化用户角色
     */
    private void initUserRole() {
        UserEntity superUser = userAuthService.getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, "super"));
        RoleEntity superRole = roleService.getOne(Wrappers.<RoleEntity>lambdaQuery().eq(RoleEntity::getCode, "super"));
        this.createUserRole(superUser, superRole);

        UserEntity adminUser = userAuthService.getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, "admin"));
        RoleEntity adminRole = roleService.getOne(Wrappers.<RoleEntity>lambdaQuery().eq(RoleEntity::getCode, "admin"));
        this.createUserRole(adminUser, adminRole);
    }


    /**
     * 初始化角色-权限绑定
     */
    private void initRolePermissions() {
        RoleEntity superRole = roleService.getOne(Wrappers.<RoleEntity>lambdaQuery().eq(RoleEntity::getCode, "super"));
        this.createRolePermission(superRole, "*");

        RoleEntity adminRole = roleService.getOne(Wrappers.<RoleEntity>lambdaQuery().eq(RoleEntity::getCode, "admin"));
        this.createRolePermission(adminRole, "*");
    }


    /**
     * 初始化用户
     */
    private void initUsers() {
        this.createUser("super", "超级管理员");
        this.createUser("admin", "系统管理员");
    }

    private void createRole(String name, String code) {
        RoleEntity role = roleService.getOne(
                Wrappers.<RoleEntity>lambdaQuery()
                        .eq(RoleEntity::getCode, code)
        );
        if (Objects.nonNull(role)) {
            return;
        }
        role = new RoleEntity();
        role.setName(name);
        role.setCode(code);
        boolean ok = roleService.save(role);
        if (!ok) {
            String message = String.format("初始化: 角色 '%s' 创建失败！", name);
            throw new RuntimeException(message);
        }
        log.info("初始化: 角色 '{}' 创建完成", name);
    }

    private void createUserRole(UserEntity user, RoleEntity role) {
        UserRoleEntity userRole = userRoleService.getOne(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .eq(UserRoleEntity::getUserId, user.getId())
        );
        if (Objects.nonNull(userRole)) {
            return;
        }
        userRole = new UserRoleEntity();
        userRole.setUserId(user.getId());
        userRole.setRoleId(user.getId());
        boolean ok = userRoleService.save(userRole);
        if (!ok) {
            String message = String.format("初始化: 用户 '%s' 绑定角色 '%s' 失败！", user.getName(), role.getName());
            throw new RuntimeException(message);
        }
        log.info("初始化: 用户 '{}' 绑定角色 '{}' 完成", user.getName(), role.getName());
    }

    private void createRolePermission(RoleEntity role, String permission) {
        RolePermissionEntity rolePermission = rolePermissionService.getOne(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .eq(RolePermissionEntity::getRoleId, role.getId())
        );
        if (Objects.nonNull(rolePermission)) {
            return;
        }
        rolePermission = new RolePermissionEntity();
        rolePermission.setRoleId(role.getId());
        rolePermission.setPermissions(permission);
        boolean ok = rolePermissionService.save(rolePermission);
        if (!ok) {
            String message = String.format("初始化: 角色 '%s' 绑定权限 '%s' 失败！", role.getName(), permission);
            throw new RuntimeException(message);
        }
        log.info("初始化: 角色 '{}' 绑定权限 '{}' 完成", role.getName(), permission);
    }

    private void createUser(String username, String nickname) {
        UserEntity user = userAuthService.getUserByUsername(username);
        if (Objects.nonNull(user)) {
            return;
        }
        user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(username));
        user.setNickname(nickname);
        user.setName(nickname);
        user.setIntroduction("");
        user.setAvatar("");
        user.setGender(UserGender.MALE.getCode());
        user.setBirthday(LocalDate.now());
        user.setPhone("");
        user.setEmail("");
        user.setPwdExpDate(LocalDate.now().plusMonths(3));
        boolean ok = userAuthService.save(user);
        if (!ok) {
            String message = String.format("用户 '%s(%s)' 初始化失败！", nickname, username);
            throw new RuntimeException(message);
        }
        log.info("用户 '{}({})' 初始化完成", nickname, username);
    }
}
