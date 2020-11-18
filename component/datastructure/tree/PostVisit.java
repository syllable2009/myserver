package com.neo.datastructure.tree;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 后根遍历:从左到右的次序行根遍历根结点的第一棵子树->访问根结点
 *
 * @author jiaxiaopeng
 * 2020-11-17 18:35
 **/

@Slf4j
public class PostVisit implements Visit {

    @Override
    public void visit(TreeNode tree) {
        if (!tree.isEmpty()) {
            List<TreeNode> children = tree.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                for (TreeNode t : children) {
                    visit(t);
                }
            }
            print(tree);
        }
    }

    private static void print(TreeNode treeNode) {
        log.info("{}", treeNode.getData());
    }

}
