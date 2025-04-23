package com.onezol.vertex.framework.common.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.onezol.vertex.framework.common.constant.DefaultPage.DEFAULT_PAGE_NUMBER;
import static com.onezol.vertex.framework.common.constant.DefaultPage.DEFAULT_PAGE_SIZE;

@Data
public class BasePayload implements Payload {

    @Schema(description = "主键ID")
    private Long id;

}
