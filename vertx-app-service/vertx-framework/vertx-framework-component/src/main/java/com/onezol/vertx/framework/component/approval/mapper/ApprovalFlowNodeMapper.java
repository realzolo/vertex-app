package com.onezol.vertx.framework.component.approval.mapper;

import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertx.framework.component.approval.model.entity.ApprovalFlowNodeEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApprovalFlowNodeMapper extends BaseMapper<ApprovalFlowNodeEntity> {
}
