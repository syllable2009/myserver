package com.neo.datastructure.tree;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 中根遍历:从左->访问根结点->右的次序行根遍历根结点的第一棵子树
 * 只适合二叉树
 *
 * @author jiaxiaopeng
 * 2020-11-17 18:35
 **/

@Slf4j
public class BinaryVisit {


    public static void midVisit(BinaryTree tree) {
        if (Objects.isNull(tree) || tree.isEmpty()) {
            return;
        }
        midVisit(tree.getLeft());
        print(tree);
        midVisit(tree.getRight());

    }

    public static void leftVisit(BinaryTree tree) {
        if (Objects.isNull(tree) || tree.isEmpty()) {
            return;
        }
        print(tree);
        leftVisit(tree.getLeft());
        leftVisit(tree.getRight());
    }

    public static void rightVisit(BinaryTree tree) {
        if (Objects.isNull(tree) || tree.isEmpty()) {
            return;
        }
        rightVisit(tree.getLeft());
        rightVisit(tree.getRight());
        print(tree);
    }

    public static void levelVisit(BinaryTree tree) {
        if (Objects.isNull(tree) || tree.isEmpty()) {
            return;
        }
        Queue<BinaryTree> queue = new LinkedBlockingQueue<>();
        queue.offer(tree);
        while (!queue.isEmpty()) {
            BinaryTree node = queue.poll();
            print(node);
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
    }

    public static void depthVisit(BinaryTree tree) {
        if (Objects.isNull(tree) || tree.isEmpty()) {
            return;
        }
        Stack<BinaryTree> stack = new Stack<BinaryTree>();
        stack.add(tree);
        while (!stack.isEmpty()) {
            BinaryTree node = stack.pop();
            print(node);
            //先往栈中压入右节点，再压左节点，这样出栈就是先左节点后右节点了。
            if (Objects.nonNull(node.getRight())) {
                stack.push(node.getRight());
            }
            if (Objects.nonNull(node.getLeft())) {
                stack.push(node.getLeft());
            }
        }
    }

    private static void print(BinaryTree treeNode) {
        log.info("{}", treeNode.getData());
    }

}
