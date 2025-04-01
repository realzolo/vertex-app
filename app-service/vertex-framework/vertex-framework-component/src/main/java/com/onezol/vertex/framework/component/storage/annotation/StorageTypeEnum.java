package com.onezol.vertex.framework.component.storage.annotation;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "存储类型")
@Getter
@AllArgsConstructor
public enum StorageTypeEnum implements Enumeration<Integer> {

    LOCAL("本地存储", 1),

    S3("兼容S3协议存储", 2);

    private final String name;

    @EnumValue
    private final Integer value;

}
