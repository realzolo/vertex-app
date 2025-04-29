package com.onezol.vertx.framework.component.notice.mapper;

import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertx.framework.component.notice.model.NoticeEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper extends BaseMapper<NoticeEntity> {
}
