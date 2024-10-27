package com.onezol.vertex.framework.security.biz.runner;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.constant.enums.UserGender;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import com.onezol.vertex.framework.security.api.service.RoleService;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
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
@Component
public class InitializationRunner implements ApplicationRunner {

    private final RoleService roleService;

    private final RolePermissionService rolePermissionService;

    private final UserAuthService userAuthService;

    private final PasswordEncoder passwordEncoder;

    public InitializationRunner(RoleService roleService, RolePermissionService rolePermissionService, UserAuthService userAuthService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.rolePermissionService = rolePermissionService;
        this.userAuthService = userAuthService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        this.initRoles();
        this.initRolePermissions();
        this.initUsers();
    }

    /**
     * 初始化角色
     */
    private void initRoles() {
        this.createRole("超级管理员");
        this.createRole("系统管理员");
        this.createRole("普通用户");
    }


    /**
     * 初始化角色-权限绑定
     */
    private void initRolePermissions() {
        RoleEntity superRole = roleService.getOne(Wrappers.<RoleEntity>lambdaQuery().eq(RoleEntity::getName, "超级管理员"));
        this.createRolePermission(superRole.getId(), "*");

        RoleEntity adminRole = roleService.getOne(Wrappers.<RoleEntity>lambdaQuery().eq(RoleEntity::getName, "系统管理员"));
        this.createRolePermission(adminRole.getId(), "*");

        RoleEntity userRole = roleService.getOne(Wrappers.<RoleEntity>lambdaQuery().eq(RoleEntity::getName, "普通用户"));
        this.createRolePermission(userRole.getId(), "*");
    }


    /**
     * 初始化用户
     */
    private void initUsers() {
        this.createUser("super", "超级管理员");
        this.createUser("admin", "系统管理员");
        this.createUser("user", "普通用户");
    }

    private void createRole(String name) {
        RoleEntity role = roleService.getOne(
                Wrappers.<RoleEntity>lambdaQuery()
                        .eq(RoleEntity::getName, name)
        );
        if (Objects.nonNull(role)) {
            return;
        }
        role = new RoleEntity();
        role.setName(name);
        boolean ok = roleService.save(role);
        if (!ok) {
            String message = String.format("角色 '%s' 初始化失败！", name);
            throw new RuntimeException(message);
        }
        log.info("角色 '{}' 初始化完成", name);
    }

    private void createRolePermission(long roleId, String permission) {
        RoleEntity role = roleService.getById(roleId);
        if (Objects.isNull(role)) {
            throw new RuntimeException(String.format("角色 '%s' 不存在！", roleId));
        }
        RolePermissionEntity rolePermission = rolePermissionService.getOne(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .eq(RolePermissionEntity::getRoleId, roleId)
        );
        if (Objects.nonNull(rolePermission)) {
            return;
        }
        rolePermission = new RolePermissionEntity();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissions(permission);
        boolean ok = rolePermissionService.save(rolePermission);
        if (!ok) {
            String message = String.format("角色 '%s' 初始化绑定权限 '%s' 失败！", role.getName(), permission);
            throw new RuntimeException(message);
        }
        log.info("角色 '{}' 初始化绑定权限 '{}' 完成", role.getName(), permission);
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
