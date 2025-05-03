package com.onezol.vertx.framework.component.upvote.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import com.onezol.vertx.framework.common.model.entity.BaseEntity;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpvoteSummary extends BaseDTO {

    @Schema(description = "点赞对象类型")
    private UpvoteObjectType objectType;

    @Schema(description = "点赞对象ID")
    private Long objectId;

    @Schema(description = "点赞数量")
    private Long count;

}
