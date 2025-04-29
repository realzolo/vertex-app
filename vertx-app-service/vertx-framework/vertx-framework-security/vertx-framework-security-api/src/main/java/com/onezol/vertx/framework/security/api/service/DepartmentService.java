package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.mvc.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.Department;
import com.onezol.vertx.framework.security.api.model.entity.DepartmentEntity;
import com.onezol.vertx.framework.security.api.model.payload.DepartmentSavePayload;

public interface DepartmentService extends BaseService<DepartmentEntity> {

    /**
     * 创建或更新部门信息
     */
    Department createOrUpdate(DepartmentSavePayload payload);

}
