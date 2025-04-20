package com.onezol.vertex.framework.component.notice.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_notice")
public class Notice extends BaseDTO {

    @Schema(name = "标题")
    @TableField("title")
    private String title;

    @Schema(name = "内容")
    @TableField("content")
    private String content;

    @Schema(name = "类型")
    @TableField("type")
    private Integer type;

    @Schema(name = "生效时间")
    @TableField("effective_time")
    private LocalDateTime effectiveTime;

    @Schema(name = "终止时间")
    @TableField("terminate_time")
    private LocalDateTime terminateTime;

    @Schema(name = "通知范围")
    @TableField("notice_scope")
    private Integer noticeScope;

    @Schema(name = "通知用户")
    private List<Long> noticeUsers;

    @Schema(name = "状态")
    private Integer status;

}
