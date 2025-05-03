package com.onezol.vertx.framework.component.upvote.model.dto;

import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpvoteRecord extends BaseDTO {

    @Schema(description = "点赞对象类型")
    private UpvoteObjectType objectType;

    @Schema(description = "点赞对象ID")
    private Long objectId;

    @Schema(description = "点赞用户ID")
    private Long userId;

}
