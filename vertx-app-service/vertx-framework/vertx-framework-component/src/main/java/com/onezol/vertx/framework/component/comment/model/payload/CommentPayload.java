package com.onezol.vertx.framework.component.comment.model.payload;

import com.onezol.vertx.framework.common.model.payload.BasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentPayload extends BasePayload {

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "关联对象")
    private Long objectId;

    @Schema(description = "内容")
    private String content;

}
