package com.onezol.vertx.framework.component.dictionary.mapper;

import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertx.framework.component.dictionary.model.DictionaryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictionaryMapper extends BaseMapper<DictionaryEntity> {
}
