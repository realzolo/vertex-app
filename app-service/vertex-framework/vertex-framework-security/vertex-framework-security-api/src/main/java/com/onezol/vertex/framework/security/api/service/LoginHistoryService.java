package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.enumeration.LoginTypeEnum;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.LoginHistoryEntity;

public interface LoginHistoryService extends BaseService<LoginHistoryEntity> {

    /**
     * 创建登录记录
     *
     * @param user 用户信息
     */
    void createLoginRecord(User user, LoginTypeEnum loginType);

}
