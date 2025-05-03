package com.onezol.vertx.framework.component.comment.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.model.entity.LogicalBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "app_comment", autoResultMap = true)
public class CommentEntity extends LogicalBaseEntity {

    @Schema(description = "父级ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "业务模块")
    @TableField("business_type")
    private String businessType;

    @Schema(description = "评论对象ID")
    @TableField("object_id")
    private Long objectId;

    @Schema(description = "内容")
    @TableField("content")
    private String content;

    @Schema(description = "评论人地址")
    @TableField("address")
    private String address;

    @Schema(description = "搜索路径")
    @TableField("path")
    private String path;

}
