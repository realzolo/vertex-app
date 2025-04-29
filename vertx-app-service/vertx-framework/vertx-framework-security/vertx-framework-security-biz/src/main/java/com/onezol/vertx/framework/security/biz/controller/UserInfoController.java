package com.onezol.vertx.framework.security.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.mvc.controller.BaseController;
import com.onezol.vertx.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertx.framework.security.api.context.AuthenticationContext;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.UserEntity;
import com.onezol.vertx.framework.security.api.model.payload.UserQueryPayload;
import com.onezol.vertx.framework.security.api.model.payload.UserSavePayload;
import com.onezol.vertx.framework.security.api.service.UserInfoService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
        UserIdentity userIdentity = AuthenticationContext.get();
        User user = userInfoService.getUserById(userIdentity.getUserId());
        return ResponseHelper.buildSuccessfulResponse(user);
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID查询用户信息")
    @RestrictAccess
    @GetMapping("/{id}")
    public GenericResponse<User> getUserInfo(@PathVariable(value = "id") Long userId) {
        User user = userInfoService.getUserById(userId);
        return ResponseHelper.buildSuccessfulResponse(user);
    }

    @Operation(summary = "创建用户", description = "创建用户")
    @PostMapping("/create")
    public GenericResponse<Void> create(@RequestBody @Valid UserSavePayload payload) {
        userInfoService.createOrUpdateUser(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "修改用户信息", description = "修改用户信息")
    @RestrictAccess
    @PutMapping("/{id}")
    public GenericResponse<User> updateUserInfo(
            @PathVariable(value = "id") Long userId,
            @RequestBody UserSavePayload payload
    ) {
        UserEntity userEntity = userInfoService.getById(userId);
        if (Objects.isNull(userEntity)) {
            ResponseHelper.buildFailedResponse("用户不存在");
        }
        return ResponseHelper.buildSuccessfulResponse(userInfoService.updateUserInfo(payload));
    }

    @Operation(summary = "删除用户", description = "删除用户")
    @RestrictAccess
    @DeleteMapping("/{id}")
    public GenericResponse<Void> deleteUser(@PathVariable(value = "id") Long userId) {
        userInfoService.deleteUser(userId);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "获取用户字典", description = "获取用户字典列表")
    @RestrictAccess
    @GetMapping("/dict")
    public GenericResponse<List<DictionaryEntry>> getUserDict() {
        List<DictionaryEntry> userDict = userInfoService.getUserDict();
        return ResponseHelper.buildSuccessfulResponse(userDict);
    }

    @Operation(summary = "获取用户列表", description = "条件查询用户列表")
    @RestrictAccess
    @GetMapping("/page")
    public GenericResponse<PagePack<User>> getUserPage(
            @RequestParam(value = "pageNumber", required = false) Long pageNumber,
            @RequestParam(value = "pageSize", required = false) Long pageSize,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime
    ) {
        UserQueryPayload payload = new UserQueryPayload();
        payload.setDepartmentId(departmentId);
        payload.setKeyword(keyword);
        payload.setStatus(status);
        payload.setStartTime(startTime);
        payload.setEndTime(endTime);

        Page<UserEntity> page = this.getPageObject(pageNumber, pageSize);
        PagePack<User> pack = userInfoService.getUserPage(page, payload);
        return ResponseHelper.buildSuccessfulResponse(pack);
    }

    @Operation(summary = "获取用户列表", description = "条件查询用户列表")
    @RestrictAccess
    @GetMapping("/unbound-role/page")
    public GenericResponse<PagePack<User>> getUserPage(
            @RequestParam(value = "pageNumber", required = false) Long pageNumber,
            @RequestParam(value = "pageSize", required = false) Long pageSize,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "roleId", required = false) Long roleId
    ) {
        UserQueryPayload payload = new UserQueryPayload();

        Page<UserEntity> page = this.getPageObject(pageNumber, pageSize);
        payload.setDepartmentId(departmentId);
        payload.setRoleId(roleId);
        return ResponseHelper.buildSuccessfulResponse(userInfoService.getUnboundRoleUserPage(page, payload));
    }

}
