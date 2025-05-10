package com.onezol.vertx.framework.security.biz.controller;

import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.skeleton.controller.BaseController;
import com.onezol.vertx.framework.security.api.context.UserIdentityContext;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.UserEntity;
import com.onezol.vertx.framework.security.api.model.payload.UserProfileBasicInfUpdatePayload;
import com.onezol.vertx.framework.security.api.service.UserProfileService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "用户概况")
@Validated
@RestController
@RequestMapping("/user/profile")
public class UserProfileController extends BaseController<UserEntity> {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Operation(summary = "修改用户信息", description = "修改用户基础信息：昵称、性别")
    @PatchMapping("/basic/info")
    public GenericResponse<User> updateUserProfile(@RequestBody @Valid UserProfileBasicInfUpdatePayload payload) {
        UserIdentity identity = UserIdentityContext.get();
        payload.setId(identity.getUserId());
        User user = userProfileService.updateUserProfile(payload);
        return ResponseHelper.buildSuccessfulResponse(user);
    }

    @Operation(summary = "修改用户头像", description = "修改用户头像")
    @PatchMapping("/avatar")
    public GenericResponse<User> updateUserAvatar(@RequestParam("file") MultipartFile file) {
        UserIdentity identity = UserIdentityContext.get();
        User user = userProfileService.updateUserAvatar(identity.getUserId(), file);
        return ResponseHelper.buildSuccessfulResponse(user);
    }

}
