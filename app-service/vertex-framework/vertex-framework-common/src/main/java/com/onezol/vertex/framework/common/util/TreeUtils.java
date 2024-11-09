package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.model.TreeNode;

import java.util.ArrayList;
import java.util.List;

public final class TreeUtils {

    public static List<TreeNode> buildTree(List<TreeNode> nodes, Long rootId) {
        List<TreeNode> treeNodes = new ArrayList<>();
        for (TreeNode node : nodes) {
            if (node.getParentId().equals(rootId)) {
                treeNodes.add(node);
            }
        }
        for (TreeNode node : treeNodes) {
            List<TreeNode> children = buildTree(nodes, node.getId());
            if (!children.isEmpty()) {
                node.setChildren(children);
            }
        }
        return treeNodes;
    }

}
