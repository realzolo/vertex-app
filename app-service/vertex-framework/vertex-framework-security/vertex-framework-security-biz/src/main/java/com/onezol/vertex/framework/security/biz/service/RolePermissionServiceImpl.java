package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.security.api.mapper.RolePermissionMapper;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

}
