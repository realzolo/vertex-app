package com.onezol.vertx.framework.component.notice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "通知公告c参数")
public class NoticeQueryPayload implements Serializable {

    @Schema(name = "ID")
    private Long id;

    @Schema(name = "标题")
    private String title;

    @Schema(name = "类型")
    private Integer type;

}
