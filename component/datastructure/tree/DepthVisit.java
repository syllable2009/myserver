package com.neo.datastructure.tree;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * 深度遍历
 *
 * @author jiaxiaopeng
 * 2020-11-17 18:35
 **/

@Slf4j
public class DepthVisit implements Visit {

    @Override
    public void visit(TreeNode tree) {
        if (Objects.nonNull(tree) && !tree.isEmpty()) {
            Stack<TreeNode> stack = new Stack<TreeNode>();
            stack.push(tree);
            TreeNode node = null;
            List<TreeNode> children = null;
            while (!stack.isEmpty()) {
                node = stack.pop();
                //先往栈中压入右节点，再压左节点，这样出栈就是先左节点后右节点了。
                print(node);
                children = node.getChildren();
                if (CollectionUtils.isNotEmpty(children)) {

                    for (int i = children.size() - 1; i >= 0; i--) {
                        stack.add(children.get(i));
                    }
//                    for (TreeNode t : children) {
//                        stack.add(t);
//                    }
                }
            }
        }
    }

    private static void print(TreeNode treeNode) {
        log.info("{}", treeNode.getData());
    }

}
