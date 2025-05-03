package com.onezol.vertx.framework.component.commet.model.dto;

import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import com.onezol.vertx.framework.security.api.model.dto.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseDTO {

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "所属模块")
    private String businessType;

    @Schema(description = "关联对象")
    private Long objectId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "评论人IP地址")
    private String address;

    @Schema(description = "评论人")
    private User author;

    @Schema(description = "点赞数")
    private Long upvotes;

    @Schema(description = "是否点赞")
    private Boolean upvoted;

    @Schema(description = "回复评论列表")
    private List<Comment> replies;

    @Schema(name = "创建人ID")
    private Long creator;

    @Schema(name = "创建时间")
    private LocalDateTime createTime;


}
