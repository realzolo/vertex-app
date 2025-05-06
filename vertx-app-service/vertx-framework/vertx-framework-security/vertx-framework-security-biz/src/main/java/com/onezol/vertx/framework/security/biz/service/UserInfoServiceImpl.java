package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.constant.enumeration.AccountStatus;
import com.onezol.vertx.framework.common.constant.enumeration.SystemRoleType;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.exception.RuntimeServiceException;
import com.onezol.vertx.framework.common.model.DataPairRecord;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.security.api.model.dto.Department;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.*;
import com.onezol.vertx.framework.security.api.model.payload.UserQueryPayload;
import com.onezol.vertx.framework.security.api.model.payload.UserSavePayload;
import com.onezol.vertx.framework.security.api.service.*;
import com.onezol.vertx.framework.security.biz.mapper.UserMapper;
import com.onezol.vertx.framework.support.cache.RedisCache;
import com.onezol.vertx.framework.support.support.RedisKeyHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserMapper, UserEntitySoft> implements UserInfoService {

    private final PasswordEncoder passwordEncoder;
    private final RedisCache redisCache;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final UserRoleService userRoleService;
    private final UserDepartmentService userDepartmentService;

    public UserInfoServiceImpl(PasswordEncoder passwordEncoder, RedisCache redisCache, DepartmentService departmentService, RoleService roleService, PermissionService permissionService, UserRoleService userRoleService, UserDepartmentService userDepartmentService) {
        this.passwordEncoder = passwordEncoder;
        this.redisCache = redisCache;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.userRoleService = userRoleService;
        this.userDepartmentService = userDepartmentService;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public User getUserById(long userId) {
        UserEntitySoft entity = this.getById(userId);
        if (Objects.isNull(entity)) return null;
        User user = BeanUtils.toBean(entity, User.class);
        this.fillUserIdentity(user);
        return user;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public User getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new InvalidParameterException("用户名不可为空");
        }
        UserEntitySoft entity = this.getOne(
                Wrappers.<UserEntitySoft>lambdaQuery()
                        .eq(UserEntitySoft::getUsername, username)
        );
        if (Objects.isNull(entity)) return null;
        User user = BeanUtils.toBean(entity, User.class);
        this.fillUserIdentity(user);
        return user;
    }

    /**
     * 根据用户邮箱获取用户信息
     *
     * @param email 用户邮箱
     * @return 用户信息
     */
    @Override
    public User getUserByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new InvalidParameterException("邮箱不可为空");
        }
        UserEntitySoft entity = this.getOne(
                Wrappers.<UserEntitySoft>lambdaQuery()
                        .eq(UserEntitySoft::getEmail, email)
        );
        if (Objects.isNull(entity)) return null;
        User user = BeanUtils.toBean(entity, User.class);
        this.fillUserIdentity(user);
        return user;
    }

    /**
     * 根据用户ID列表取用户信息列表
     *
     * @param userIds 用户ID列表
     * @return 用户信息列表
     */
    @Override
    public List<User> getUsersInfo(List<Long> userIds) {
        userIds = userIds.stream().distinct().toList();

        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            User user = this.getUserById(userId);
            users.add(user);
        }
        return users;
    }

    /**
     * 创建/更新用户
     *
     * @param payload 用户参数
     */
    @Override
    @Transactional
    public void createOrUpdateUser(UserSavePayload payload) {
        UserEntitySoft userEntity = BeanUtils.toBean(payload, UserEntitySoft.class);
        userEntity.setPassword(passwordEncoder.encode(payload.getPassword()));
        userEntity.setEmail(Objects.nonNull(payload.getEmail()) ? payload.getEmail().toLowerCase() : null);

        String username = payload.getUsername();
        String email = payload.getEmail();

        // 用户唯一性校验
        if (Objects.nonNull(this.getUserByUsername(username))) {
            throw new InvalidParameterException("当前用户名已存在");
        }
        if (Objects.nonNull(this.getUserByEmail(email))) {
            throw new InvalidParameterException("当前邮箱已绑定其它账号，请更换邮箱");
        }

        // 部门信息合法性校验
        if (Objects.isNull(departmentService.getById(payload.getDepartmentId()))) {
            throw new InvalidParameterException("当前部门不存在，请确认部门信息");
        }

        // 角色列表合法性校验
        List<String> roleCodes = payload.getRoleCodes();
        List<RoleEntity> roleEntities = roleService.list(
                Wrappers.<RoleEntity>lambdaQuery()
                        .in(RoleEntity::getCode, roleCodes)
        );
        if (roleEntities.size() != roleCodes.size()) {
            throw new InvalidParameterException("当前角色不存在，请确认角色信息");
        }

        // 创建或更新用户信息
        this.saveOrUpdate(userEntity);
        this.bindUserDepartment(userEntity.getId(), payload.getDepartmentId());
        this.bindUserRole(userEntity.getId(), payload.getRoleCodes());
    }

    /**
     * 修改用户信息
     */
    @Override
    public User updateUserInfo(UserSavePayload payload) {
        if (Objects.isNull(payload) || Objects.isNull(payload.getId())) {
            throw new InvalidParameterException("用户信息不可为空");
        }
        UserEntitySoft entity = BeanUtils.toBean(payload, UserEntitySoft.class);
        boolean ok = this.updateById(entity);
        if (!ok) {
            throw new RuntimeServiceException("修改用户信息失败");
        }
        List<String> roleCodes = payload.getRoleCodes();

        // 解绑用户角色
        userRoleService.unassignAllRolesFromUser(payload.getId());
        // 绑定用户角色
        userRoleService.assignRolesForUser(payload.getId(), roleCodes);

        return this.getUserById(payload.getId());
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    @Override
    public void deleteUser(Long userId) {
        if (Objects.isNull(userId)) {
            throw new InvalidParameterException("用户ID不可为空");
        }
        UserEntitySoft user = this.getById(userId);
        if (Objects.isNull(user)) {
            throw new InvalidParameterException("用户不存在");
        }

        // 判断当前用户是否满足删除条件：...
        // ...

        // 解绑用户角色
        userRoleService.unassignAllRolesFromUser(userId);

        // 删除Redis相关缓存
        // 1. 删除用户Token缓存
        String userTokenRedisKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, String.valueOf(userId));
        redisCache.deleteObject(userTokenRedisKey);
        // 2. 删除用户信息缓存
        String userInfoRedisKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, String.valueOf(userId));
        redisCache.deleteObject(userInfoRedisKey);

        // 删除用户
        this.removeById(userId);
    }

    @Override
    public List<DictionaryEntry> getUserDict() {
        List<UserEntitySoft> userEntities = this.list(
                Wrappers.<UserEntitySoft>lambdaQuery()
                        .eq(UserEntitySoft::getStatus, AccountStatus.ACTIVE)
        );
        return userEntities.stream()
                .map(entity -> DictionaryEntry.of(entity.getNickname(), entity.getId()))
                .toList();
    }

    /**
     * 获取用户列表
     */
    @Override
    public PagePack<User> getUserPage(Page<UserEntitySoft> page, UserQueryPayload payload) {
        Page<UserEntitySoft> userPage = this.baseMapper.queryUserPage(page, payload);
        PagePack<User> pack = PagePack.from(userPage, User.class);
        Collection<User> users = pack.getItems();

        for (User user : users) {
            List<Role> userRoles = userRoleService.getUserRoles(user.getId());
            if (userRoles != null && !userRoles.isEmpty()) {
                List<DataPairRecord> roles = new ArrayList<>();
                for (Role role : userRoles) {
                    DataPairRecord roleRecord = new DataPairRecord(role.getId(), role.getName(), role.getCode());
                    roles.add(roleRecord);
                }
                user.setRoles(roles);
            }
            Department department = userDepartmentService.getUserDepartment(user.getId());
            if (department != null) {
                DataPairRecord departmentRecord = new DataPairRecord(department.getId(), department.getName());
                user.setDepartment(departmentRecord);
            }
        }

        return pack;
    }

    /**
     * 获取未绑定角色的用户列表
     */
    @Override
    public PagePack<User> getUnboundRoleUserPage(Page<UserEntitySoft> page, UserQueryPayload payload) {
        Page<UserEntitySoft> userPage = this.baseMapper.queryUnboundRoleUserPage(page, payload);
        PagePack<User> pack = PagePack.from(userPage, User.class);
        Collection<User> users = pack.getItems();

        for (User user : users) {
            List<Role> userRoles = userRoleService.getUserRoles(user.getId());
            if (userRoles != null && !userRoles.isEmpty()) {
                List<DataPairRecord> roles = new ArrayList<>();
                for (Role role : userRoles) {
                    DataPairRecord roleRecord = new DataPairRecord(role.getId(), role.getName(), role.getCode());
                    roles.add(roleRecord);
                }
                user.setRoles(roles);
            }
            Department department = userDepartmentService.getUserDepartment(user.getId());
            if (department != null) {
                DataPairRecord departmentRecord = new DataPairRecord(department.getId(), department.getName());
                user.setDepartment(departmentRecord);
            }
        }

        return pack;
    }

    /**
     * 填充用户身份信息：部门、角色、权限
     */
    private void fillUserIdentity(User user) {
        // 获取用户部门
        Department department = userDepartmentService.getUserDepartment(user.getId());
        if (department != null) {
            DataPairRecord departmentRecord = new DataPairRecord(department.getId(), department.getName());
            user.setDepartment(departmentRecord);
        }
        // 获取用户角色
        List<Role> userRoles = userRoleService.getUserRoles(user.getId());
        List<DataPairRecord> roles = new ArrayList<>();
        for (Role role : userRoles) {
            DataPairRecord roleRecord = new DataPairRecord(role.getId(), role.getName(), role.getCode());
            roles.add(roleRecord);
        }
        user.setRoles(roles);
        // 获取用户权限
        Set<String> roleCodes = userRoles.stream().map(Role::getCode).collect(Collectors.toSet());
        if (roleCodes.contains(SystemRoleType.SUPER.getValue())) {
            user.setPermissions(List.of("*:*:*"));
        } else {
            List<Long> roleIds = userRoles.stream().map(Role::getId).toList();
            List<String> rolePermissionKeys = permissionService.getRolePermissionKeys(roleIds);
            List<String> permissions = List.copyOf(rolePermissionKeys);
            user.setPermissions(permissions);
        }
    }

    /**
     * 人员-部门关联
     */
    private void bindUserDepartment(Long userId, Long departmentId) {
        // 判断是否已经存在关联关系
        UserDepartmentEntity relation = userDepartmentService.getOne(
                Wrappers.<UserDepartmentEntity>lambdaQuery()
                        .select(UserDepartmentEntity::getId, UserDepartmentEntity::getUserId, UserDepartmentEntity::getDepartmentId)
                        .eq(UserDepartmentEntity::getUserId, userId)
        );
        if (Objects.isNull(relation)) {
            UserDepartmentEntity entity = new UserDepartmentEntity();
            entity.setUserId(userId);
            entity.setDepartmentId(departmentId);
            userDepartmentService.save(entity);
        } else {
            relation.setDepartmentId(departmentId);
            userDepartmentService.updateById(relation);
        }
    }

    /**
     * 人员-角色关联
     */
    private void bindUserRole(Long userId, List<String> roleCodes) {
        // 判断是否已经存在关联关系
        List<UserRoleEntity> relations = userRoleService.list(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .select(UserRoleEntity::getId, UserRoleEntity::getUserId, UserRoleEntity::getRoleId)
                        .eq(UserRoleEntity::getUserId, userId)
        );
        List<RoleEntity> roleEntities = roleService.list(
                Wrappers.<RoleEntity>lambdaQuery()
                        .select(RoleEntity::getId, RoleEntity::getCode)
                        .in(!roleCodes.isEmpty(), RoleEntity::getCode, roleCodes)
        );

        // 删除存在的关联关系
        List<Long> shouldDeleteRelation = relations.stream().map(UserRoleEntity::getId).toList();
        userRoleService.removeByIds(shouldDeleteRelation);

        // 创建新的关联关系
        List<UserRoleEntity> shouldCreateEntities = new ArrayList<>();
        for (String roleCode : roleCodes) {
            Long roleId = roleEntities.stream().filter(role -> role.getCode().equals(roleCode)).findFirst().get().getId();
            UserRoleEntity entity = new UserRoleEntity();
            entity.setUserId(userId);
            entity.setRoleId(roleId);
            shouldCreateEntities.add(entity);
        }
        userRoleService.saveBatch(shouldCreateEntities);
    }

}
