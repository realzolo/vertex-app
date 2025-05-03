package com.onezol.vertx.framework.component.upvote.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "点赞状态：点赞/取消点赞")
@Getter
@RequiredArgsConstructor
public enum UpvoteStatus implements StandardEnumeration<Integer> {

    UPVOTE("点赞", 1),
    CANCEL_UPVOTE("取消点赞", 0);

    private final String name;

    @EnumValue
    private final Integer value;

}
