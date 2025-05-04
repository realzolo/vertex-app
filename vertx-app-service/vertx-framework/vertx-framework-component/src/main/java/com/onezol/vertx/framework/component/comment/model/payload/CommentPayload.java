package com.onezol.vertx.framework.component.comment.model.payload;

import com.onezol.vertx.framework.common.model.payload.BasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentPayload extends BasePayload {

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "关联对象")
    @NotNull(message = "关联对象 ID 不能为空")
    private Long objectId;

    @Schema(description = "内容")
    @NotBlank(message = "评论内容不能为空，且不能只包含空白字符")
    private String content;

}
