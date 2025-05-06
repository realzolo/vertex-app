package com.onezol.vertx.framework.component.approval.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(name = "流程模板保存参数")
@Data
public class ApprovalFlowTemplateSavePayload implements Serializable {

    @Schema(name = "主键")
    private Long id;

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
