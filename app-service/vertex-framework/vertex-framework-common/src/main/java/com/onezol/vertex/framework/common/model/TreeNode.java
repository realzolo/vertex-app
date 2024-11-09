package com.onezol.vertex.framework.common.model;

import lombok.Data;

import java.util.List;

@Data
public class TreeNode {
    private Long id;
    private Long parentId;
    private String title;
    private String icon;
    private Boolean disabled;
    private List<TreeNode> children;
    private Object data;
}
