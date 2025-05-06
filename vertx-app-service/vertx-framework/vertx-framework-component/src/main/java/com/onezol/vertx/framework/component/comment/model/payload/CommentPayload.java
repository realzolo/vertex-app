package com.onezol.vertx.framework.component.comment.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentPayload implements Serializable {

    @Schema(description = "评论ID")
    private Long id;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "关联对象")
    @NotNull(message = "关联对象 ID 不能为空")
    private Long objectId;

    @Schema(description = "内容")
    @NotBlank(message = "评论内容不能为空，且不能只包含空白字符")
    private String content;

}
