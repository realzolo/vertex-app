package com.onezol.vertex.framework.component.approval.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.LogicalBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_flow_graph")
public class FlowGraph extends LogicalBaseEntity {

    @Schema(name = "流程名称")
    private String name;

    @Schema(name = "流程内容")
    private String content;

    @Schema(name = "流程状态")
    private Integer status;

    @Schema(name = "流程备注")
    private String remark;

}
