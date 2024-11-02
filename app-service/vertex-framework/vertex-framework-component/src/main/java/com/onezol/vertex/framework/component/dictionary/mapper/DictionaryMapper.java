package com.onezol.vertex.framework.component.dictionary.mapper;

import com.onezol.vertex.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertex.framework.component.dictionary.model.DictionaryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictionaryMapper extends BaseMapper<DictionaryEntity> {
}
