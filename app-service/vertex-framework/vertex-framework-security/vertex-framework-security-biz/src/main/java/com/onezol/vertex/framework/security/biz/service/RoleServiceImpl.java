package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.security.api.mapper.RoleMapper;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, RoleEntity> implements RoleService {

}
