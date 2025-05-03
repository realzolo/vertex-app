package com.onezol.vertx.framework.component.upvote.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "点赞对象类型")
@Getter
@RequiredArgsConstructor
public enum UpvoteObjectType implements StandardEnumeration<String> {

    COMMENT("评论", "COMMENT");

    private final String name;

    @EnumValue
    private final String value;

}
