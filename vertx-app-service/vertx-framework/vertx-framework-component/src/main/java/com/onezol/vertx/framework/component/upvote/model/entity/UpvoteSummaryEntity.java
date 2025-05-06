package com.onezol.vertx.framework.component.upvote.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "app_upvote_summary")
public class UpvoteSummaryEntity extends BaseEntity {

    @Schema(description = "点赞对象类型")
    @TableField("object_type")
    private UpvoteObjectType objectType;

    @Schema(description = "点赞对象ID")
    @TableField("object_id")
    private Long objectId;

    @Schema(description = "点赞数量")
    @TableField("count")
    private Long count;

}
