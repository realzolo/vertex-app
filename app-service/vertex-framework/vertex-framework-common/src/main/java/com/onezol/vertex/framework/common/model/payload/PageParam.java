package com.onezol.vertex.framework.common.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.onezol.vertex.framework.common.constant.DefaultPage.DEFAULT_PAGE_NUMBER;
import static com.onezol.vertex.framework.common.constant.DefaultPage.DEFAULT_PAGE_SIZE;

@Schema(description = "分页参数")
@Data
public class PageParam implements Payload {

    @Schema(description = "页码，从 1 开始", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNumber = DEFAULT_PAGE_NUMBER;

    @Schema(description = "每页条数，最大值为 100", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小值为 1")
    @Max(value = 100, message = "每页条数最大值为 100")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

}
