package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.security.api.mapper.PermissionMapper;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

}
