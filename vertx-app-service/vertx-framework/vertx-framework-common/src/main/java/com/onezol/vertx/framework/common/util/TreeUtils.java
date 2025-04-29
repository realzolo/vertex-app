package com.onezol.vertx.framework.common.util;

import com.onezol.vertx.framework.common.model.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 树工具类
 */
public final class TreeUtils {

    private TreeUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static List<TreeNode> buildTree(List<TreeNode> nodes, Long parentId) {
        List<TreeNode> treeNodes = new ArrayList<>();
        for (TreeNode node : nodes) {
            if (node.getParentId().equals(parentId)) {
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
