package com.onezol.vertx.framework.component.notice.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.onezol.vertx.framework.common.model.entity.LogicalBaseEntity;
import com.onezol.vertx.framework.component.notice.enumeration.NoticeScope;
import com.onezol.vertx.framework.component.notice.enumeration.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "app_notice", autoResultMap = true)
public class NoticeEntity extends LogicalBaseEntity {

    @Schema(name = "标题")
    @TableField("title")
    private String title;

    @Schema(name = "内容")
    @TableField("content")
    private String content;

    @Schema(name = "类型")
    @TableField("type")
    private NoticeType type;

    @Schema(name = "生效时间")
    @TableField("effective_time")
    private LocalDateTime effectiveTime;

    @Schema(name = "终止时间")
    @TableField("terminate_time")
    private LocalDateTime terminateTime;

    @Schema(name = "通知范围")
    @TableField("notice_scope")
    private NoticeScope noticeScope;

    @Schema(name = "通知用户")
    @TableField(value = "notice_users", typeHandler = JacksonTypeHandler.class)
    private List<Long> noticeUsers;

}
