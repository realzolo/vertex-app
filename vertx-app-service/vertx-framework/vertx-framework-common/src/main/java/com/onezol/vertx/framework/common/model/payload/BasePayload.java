package com.onezol.vertx.framework.common.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BasePayload implements Payload {

    @Schema(name = "主键ID")
    private Long id;

}
