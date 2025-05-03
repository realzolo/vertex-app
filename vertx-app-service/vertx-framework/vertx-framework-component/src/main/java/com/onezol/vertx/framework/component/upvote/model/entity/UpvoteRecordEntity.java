package com.onezol.vertx.framework.component.upvote.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.model.entity.BaseEntity;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "app_upvote_record", autoResultMap = true)
public class UpvoteRecordEntity extends BaseEntity {

    @Schema(description = "点赞对象类型")
    @TableField("object_type")
    private UpvoteObjectType objectType;

    @Schema(description = "点赞对象ID")
    @TableField("object_id")
    private Long objectId;

    @Schema(description = "点赞用户ID")
    @TableField("user_id")
    private Long userId;

}
