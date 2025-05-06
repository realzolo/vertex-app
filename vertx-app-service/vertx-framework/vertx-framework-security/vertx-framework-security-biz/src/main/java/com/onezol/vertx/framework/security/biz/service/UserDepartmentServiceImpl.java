package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.security.biz.mapper.UserDepartmentMapper;
import com.onezol.vertx.framework.security.api.model.dto.Department;
import com.onezol.vertx.framework.security.api.model.entity.DepartmentEntity;
import com.onezol.vertx.framework.security.api.model.entity.UserDepartmentEntity;
import com.onezol.vertx.framework.security.api.service.DepartmentService;
import com.onezol.vertx.framework.security.api.service.UserDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class UserDepartmentServiceImpl extends BaseServiceImpl<UserDepartmentMapper, UserDepartmentEntity> implements UserDepartmentService {

    private final DepartmentService departmentService;

    public UserDepartmentServiceImpl(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 根据用户ID获取用户部门信息
     *
     * @param userId 用户ID
     * @return 部门信息
     */
    @Override
    public Department getUserDepartment(Long userId) {
        UserDepartmentEntity userDepartment = this.getOne(
                Wrappers.<UserDepartmentEntity>lambdaQuery()
                        .eq(UserDepartmentEntity::getUserId, userId)
        );
        if (Objects.isNull(userDepartment)) {
            return null;
        }
        DepartmentEntity department = departmentService.getById(userDepartment.getDepartmentId());
        return BeanUtils.toBean(department, Department.class);
    }

}
