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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息")
@Validated
@RestController
@RequestMapping("/user")
public class UserInfoController extends BaseController<UserEntity> {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息")
    @RestrictAccess
    @GetMapping("/me")
    public GenericResponse<User> me() {
        AuthUser authUser = AuthenticationContext.get();
        String username = authUser.getUsername();
        return ResponseHelper.buildSuccessfulResponse(userInfoService.getUserInfo(username));
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID查询用户信息")
    @RestrictAccess
    @GetMapping("/{id}")
    public GenericResponse<User> getUserInfo(@PathVariable(value = "id") Long userId) {
        UserEntity userEntity = userInfoService.getById(userId);
        User user = BeanUtils.toBean(userEntity, User.class);
        return ResponseHelper.buildSuccessfulResponse(user);
    }

    @Operation(summary = "修改用户信息", description = "修改用户信息")
    @RestrictAccess
    @PutMapping
    public GenericResponse<User> updateUserInfo(
            @RequestBody User user
    ) {
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
    @PostMapping("/page")
    public GenericResponse<PlainPage<User>> getUserPage(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestBody(required = false) UserQueryPayload payload
    ) {
        Page<UserEntity> page = this.getPage(pageNumber, pageSize);
        return ResponseHelper.buildSuccessfulResponse(userInfoService.getUserPage(page, payload));
    }

}
