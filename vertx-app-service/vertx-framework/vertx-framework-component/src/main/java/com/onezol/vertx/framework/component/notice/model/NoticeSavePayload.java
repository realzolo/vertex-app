package com.onezol.vertx.framework.component.notice.model;

import com.onezol.vertx.framework.common.model.payload.Payload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "通知公告新增/更新参数", description = "通知公告新增/更新参数")
public class NoticeSavePayload implements Payload {

    @Schema(name = "ID")
    private Long id;

    @Schema(name = "标题")
    private String title;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "类型")
    private Integer type;

    @Schema(name = "生效时间")
    private LocalDateTime effectiveTime;

    @Schema(name = "终止时间")
    private LocalDateTime terminateTime;

    @Schema(name = "通知范围")
    private Integer noticeScope;

    @Schema(name = "通知用户")
    private List<Long> noticeUsers;

}
