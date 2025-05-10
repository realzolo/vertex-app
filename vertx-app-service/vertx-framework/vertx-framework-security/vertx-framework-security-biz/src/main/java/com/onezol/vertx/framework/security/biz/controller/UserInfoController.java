package com.onezol.vertx.framework.security.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.skeleton.controller.BaseController;
import com.onezol.vertx.framework.security.api.context.UserIdentityContext;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/me")
    @PreAuthorize("@Security.hasPermission('system:user:detail')")
    public GenericResponse<User> me() {
        UserIdentity userIdentity = UserIdentityContext.get();
        User user = userInfoService.getUserById(userIdentity.getUserId());
        return ResponseHelper.buildSuccessfulResponse(user);
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID查询用户信息")
    @GetMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:user:detail')")
    public GenericResponse<User> getUserInfo(@PathVariable(value = "id") Long userId) {
        User user = userInfoService.getUserById(userId);
        return ResponseHelper.buildSuccessfulResponse(user);
    }

    @Operation(summary = "创建用户", description = "创建用户")
    @PostMapping("/create")
    @PreAuthorize("@Security.hasPermission('system:user:create')")
    public GenericResponse<Void> create(@RequestBody @Valid UserSavePayload payload) {
        userInfoService.createOrUpdateUser(payload);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "修改用户信息", description = "修改用户信息")
    @PutMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:user:update')")
    public GenericResponse<User> updateUserInfo(
            @PathVariable(value = "id") Long userId,
            @RequestBody UserSavePayload payload
    ) {
        return ResponseHelper.buildSuccessfulResponse(userInfoService.updateUserInfo(payload));
    }

    @Operation(summary = "删除用户", description = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:user:delete')")
    public GenericResponse<Void> deleteUser(@PathVariable(value = "id") Long userId) {
        userInfoService.deleteUser(userId);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "获取用户字典", description = "获取用户字典列表")
    @GetMapping("/dict")
    @PreAuthorize("@Security.hasPermission('system:user:list')")
    public GenericResponse<List<DictionaryEntry>> getUserDict() {
        List<DictionaryEntry> userDict = userInfoService.getUserDict();
        return ResponseHelper.buildSuccessfulResponse(userDict);
    }

    @Operation(summary = "获取用户列表", description = "条件查询用户列表")
    @GetMapping("/page")
    @PreAuthorize("@Security.hasPermission('system:user:list')")
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
    @GetMapping("/unbound-role/page")
    @PreAuthorize("@Security.hasPermission('system:user:list')")
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
