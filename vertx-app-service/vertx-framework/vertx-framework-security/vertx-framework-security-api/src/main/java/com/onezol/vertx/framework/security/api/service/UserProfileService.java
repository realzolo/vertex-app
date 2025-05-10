package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.skeleton.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.UserEntity;
import com.onezol.vertx.framework.security.api.model.payload.UserProfileBasicInfUpdatePayload;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService extends BaseService<UserEntity> {

    /**
     * 修改用户基础信息
     */
    User updateUserProfile(@Valid UserProfileBasicInfUpdatePayload payload);

    /**
     * 修改用户头像
     */
    User updateUserAvatar(Long userId, MultipartFile file);

}
