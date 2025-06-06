package com.onezol.vertx.framework.component.storage.annotation;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "存储类型")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "存储类型", value = "storage_type")
public enum StorageType implements StandardEnumeration<Integer> {

    LOCAL("本地存储", 1),

    OSS("对象存储", 2);

    private final String name;

    @EnumValue
    private final Integer value;

}
