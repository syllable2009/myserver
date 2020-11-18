package com.neo.datastructure.tree;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 广度遍历
 *
 * @author jiaxiaopeng
 * 2020-11-17 18:35
 **/

@Slf4j
public class LevelVisit implements Visit {

    @Override
    public void visit(TreeNode tree) {
        if (Objects.nonNull(tree) && !tree.isEmpty()) {
            LinkedBlockingDeque<TreeNode> queue = new LinkedBlockingDeque<TreeNode>();
            queue.offer(tree);
            TreeNode node = null;
            List<TreeNode> children = null;
            while (!queue.isEmpty()) {
                node = queue.poll();
                print(node);
                children = node.getChildren();
                if (CollectionUtils.isNotEmpty(children)) {
                    for (TreeNode t : children) {
                        queue.offer(t);
                    }
                }
            }
        }
    }

    private static void print(TreeNode treeNode) {
        log.info("{}", treeNode.getData());
    }

}
