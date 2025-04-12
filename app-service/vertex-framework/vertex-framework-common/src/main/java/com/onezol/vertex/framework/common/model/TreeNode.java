package com.onezol.vertex.framework.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "TreeNode", description = "树节点")
public class TreeNode {

    @Schema(name = "ID")
    private Long id;

    @Schema(name = "ID")
    private Long key;

    @Schema(name = "父ID")
    private Long parentId;

    @Schema(name = "节点名称")
    private String title;

    @Schema(name = "图标")
    private String icon;

    @Schema(name = "是否禁用")
    private Boolean disabled;

    @Schema(name = "子节点列表")
    private List<TreeNode> children;

    @Schema(name = "节点数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    
}
