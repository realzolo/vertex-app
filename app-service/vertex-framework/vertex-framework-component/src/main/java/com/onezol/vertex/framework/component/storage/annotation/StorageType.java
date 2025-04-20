package com.onezol.vertex.framework.component.storage.annotation;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "存储类型")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "存储类型", value = "storage_type")
public enum StorageType implements StandardEnumeration<Integer> {

    LOCAL("本地存储", 1),

    S3("S3协议存储", 2);

    private final String name;

    @EnumValue
    private final Integer value;

}
