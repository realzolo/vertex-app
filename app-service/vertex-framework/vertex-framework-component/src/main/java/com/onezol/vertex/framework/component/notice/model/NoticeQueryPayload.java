package com.onezol.vertex.framework.component.notice.model;

import com.onezol.vertex.framework.common.model.payload.Payload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "通知公告c参数")
public class NoticeQueryPayload implements Payload {

    @Schema(name = "ID")
    private Long id;

    @Schema(name = "标题")
    private String title;

    @Schema(name = "类型")
    private Integer type;

}
