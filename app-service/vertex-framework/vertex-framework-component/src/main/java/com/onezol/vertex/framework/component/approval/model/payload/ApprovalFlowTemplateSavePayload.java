package com.onezol.vertex.framework.component.approval.model.payload;

import com.onezol.vertex.framework.common.model.payload.BasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "流程模板保存参数")
public class ApprovalFlowTemplateSavePayload extends BasePayload {

    @Schema(name = "流程名称")
    private String name;

    @Schema(name = "流程类型")
    private Integer type;

    @Schema(name = "流程内容")
    private String content;

    @Schema(name = "流程状态")
    private Integer status;

    @Schema(name = "流程备注")
    private String remark;

}
