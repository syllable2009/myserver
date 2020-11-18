package com.neo.datastructure.tree;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * 前根遍历:访问根结点->从左到右的次序行根遍历根结点的第一棵子树
 *
 * @author jiaxiaopeng
 * 2020-11-17 18:35
 **/

@Slf4j
public class PreVisit implements Visit {

    // 递归
    @Override
    public void visit(TreeNode root) {
        if (!root.isEmpty()) {
            print(root); // 数据
            List<TreeNode> children = root.getChildren();
            if (CollectionUtils.isEmpty(children)) {
                return;
            }
            for (TreeNode node : children) {
                if (Objects.nonNull(node)) {
                    visit(node);
                }
            }
        }
    }

    // 非递归式
    public void visit2(TreeNode tree) {
        if (Objects.isNull(tree) || tree.isEmpty()) {
            return;
        }
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(tree);
        List<TreeNode> children;
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            print(pop);
            children = pop.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(children.get(i));
                }
            }
        }


    }

    public static void print(TreeNode tree) {
        log.info("{}", tree.getData());
    }
}
