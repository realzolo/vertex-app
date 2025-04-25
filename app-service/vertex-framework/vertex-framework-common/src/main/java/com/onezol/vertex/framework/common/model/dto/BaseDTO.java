package com.onezol.vertex.framework.common.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseDTO implements DTO {

    @Schema(name = "主键ID")
    private Long id;

    @Schema(name = "创建人ID")
    private Long creator;

    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "更新人ID")
    private Long updater;

    @Schema(name = "更新时间")
    private LocalDateTime updateTime;

}
