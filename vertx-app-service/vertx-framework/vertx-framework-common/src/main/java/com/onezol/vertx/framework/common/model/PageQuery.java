package com.onezol.vertx.framework.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageQuery implements Serializable {

    @Schema(description = "页码")
    @Min(value = 1, message = "页码最小值为 {value}")
    private Long pageNumber = 1L;

    @Schema(description = "每页条数")
    @Max(value = 100, message = "每页条数（取值范围 {value}）")
    private Long pageSize = 10L;

}