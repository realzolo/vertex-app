package com.onezol.vertx.framework.security.biz.service;

import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.component.storage.service.FileUploadService;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.UserEntity;
import com.onezol.vertx.framework.security.api.model.payload.UserProfileBasicInfUpdatePayload;
import com.onezol.vertx.framework.security.api.service.UserInfoService;
import com.onezol.vertx.framework.security.api.service.UserProfileService;
import com.onezol.vertx.framework.security.biz.mapper.UserMapper;
import org.dromara.x.file.storage.core.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class UserProfileServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserProfileService {

    private final UserInfoService userInfoService;

    private final FileUploadService fileUploadService;

    public UserProfileServiceImpl(UserInfoService userInfoService, FileUploadService fileUploadService) {
        this.userInfoService = userInfoService;
        this.fileUploadService = fileUploadService;
    }

    /**
     * 修改用户信息
     */
    @Override
    public User updateUserProfile(UserProfileBasicInfUpdatePayload payload) {
        if (Objects.isNull(payload) || Objects.isNull(payload.getId())) {
            throw new InvalidParameterException("用户信息不可为空");
        }
        UserEntity entity = BeanUtils.toBean(payload, UserEntity.class);

        this.updateById(entity);

        return userInfoService.getUserById(payload.getId());
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param file   头像文件
     */
    @Override
    public User updateUserAvatar(Long userId, MultipartFile file) {
        UserEntity entity = this.getById(userId);
        String avatar = entity.getAvatar();
        if (StringUtils.isNotBlank(avatar)) {
            fileUploadService.delete(avatar);
        }
        FileInfo fileInfo = fileUploadService.upload(file);
        entity.setAvatar(fileInfo.getUrl());

        this.updateById(entity);

        return userInfoService.getUserById(userId);
    }

}
