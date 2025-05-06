package com.onezol.vertx.framework.security.biz.service;

import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.Asserts;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.security.biz.mapper.DepartmentMapper;
import com.onezol.vertx.framework.security.api.model.dto.Department;
import com.onezol.vertx.framework.security.api.model.entity.DepartmentEntity;
import com.onezol.vertx.framework.security.api.model.payload.DepartmentSavePayload;
import com.onezol.vertx.framework.security.api.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.onezol.vertx.framework.common.constant.GenericConstants.ROOT_OBJECT_PARENT_ID;

@Slf4j
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentMapper, DepartmentEntity> implements DepartmentService {

    /**
     * 创建或更新部门信息
     */
    @Override
    public Department createOrUpdate(DepartmentSavePayload payload) {
        Asserts.notNull(payload);

        DepartmentEntity entity = BeanUtils.toBean(payload, DepartmentEntity.class);

        Long parentId = payload.getParentId();
        if (parentId != null && !Objects.equals(ROOT_OBJECT_PARENT_ID, parentId)) {
            DepartmentEntity parent = this.getById(parentId);
            Asserts.notNull(parent, "父级部门不存在");
            entity.setAncestors(parent.getAncestors() + "," + parentId);
        } else {
            entity.setAncestors("");
        }

        if (payload.getId() == null) {
            this.save(entity);
        } else {
            this.updateById(entity);
        }

        return BeanUtils.toBean(entity, Department.class);
    }

}
