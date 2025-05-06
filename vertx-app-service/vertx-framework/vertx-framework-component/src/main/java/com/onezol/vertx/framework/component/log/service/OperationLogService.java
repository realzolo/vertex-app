package com.onezol.vertx.framework.component.log.service;

import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.component.log.mapper.OperationLogMapper;
import com.onezol.vertx.framework.component.log.model.entity.OperationLogEntity;
import org.springframework.stereotype.Service;

@Service
public class OperationLogService extends BaseServiceImpl<OperationLogMapper, OperationLogEntity> {
}
