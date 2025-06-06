package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.skeleton.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.Department;
import com.onezol.vertx.framework.security.api.model.entity.UserDepartmentEntity;

public interface UserDepartmentService extends BaseService<UserDepartmentEntity> {

    /**
     * 根据用户ID获取用户部门信息
     * @param userId 用户ID
     * @return 部门信息
     */
    Department getUserDepartment(Long userId);

}
