package com.onezol.vertex.framework.security.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.bean.AuthenticationContext;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.AuthUser;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.PlainPage;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserQueryPayload;
import com.onezol.vertex.framework.security.api.service.UserInfoService;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "用户信息")
@Validated
@RestController
@RequestMapping("/user")
public class UserInfoController extends BaseController<UserEntity> {

    private final UserInfoService userInfoService;
    private final UserRoleService userRoleService;

    public UserInfoController(UserInfoService userInfoService, UserRoleService userRoleService) {
        this.userInfoService = userInfoService;
        this.userRoleService = userRoleService;
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息")
    @RestrictAccess
    @GetMapping("/me")
    public GenericResponse<User> me() {
        AuthUser authUser = AuthenticationContext.get();
        long userId = authUser.getUserId();
        return ResponseHelper.buildSuccessfulResponse(userInfoService.getUserInfo(userId));
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID查询用户信息")
    @RestrictAccess
    @GetMapping("/{id}")
    public GenericResponse<User> getUserInfo(@PathVariable(value = "id") Long userId) {
        User user = userInfoService.getUserInfo(userId);
        return ResponseHelper.buildSuccessfulResponse(user);
    }

    @Operation(summary = "修改用户信息", description = "修改用户信息")
    @RestrictAccess
    @PutMapping("/{id}")
    public GenericResponse<User> updateUserInfo(
            @PathVariable(value = "id") Long userId,
            @RequestBody User user
    ) {
        UserEntity userEntity = userInfoService.getById(userId);
        if (Objects.isNull(userEntity)) {
            ResponseHelper.buildFailedResponse("用户不存在");
        }
        return ResponseHelper.buildSuccessfulResponse(userInfoService.updateUserInfo(user));
    }

    @Operation(summary = "删除用户", description = "删除用户")
    @RestrictAccess
    @DeleteMapping("/{id}")
    public GenericResponse<Void> deleteUser(@PathVariable(value = "id") Long userId) {
        userInfoService.deleteUser(userId);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "获取用户列表", description = "条件查询用户列表")
    @RestrictAccess
    @GetMapping("/page")
    public GenericResponse<PlainPage<User>> getUserPage(
            @RequestParam(value = "page", required = false) Integer pageNumber,
            @RequestParam(value = "size", required = false) Integer pageSize,
            @RequestBody(required = false) UserQueryPayload payload
    ) {
        Page<UserEntity> page = this.getPage(pageNumber, pageSize);
        return ResponseHelper.buildSuccessfulResponse(userInfoService.getUserPage(page, payload));
    }

}
