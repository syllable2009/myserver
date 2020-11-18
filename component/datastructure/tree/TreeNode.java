package com.neo.datastructure.tree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author jiaxiaopeng
 * 2020-11-16 20:14
 **/
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeNode<T> implements Serializable {

    private T data;
    private List<TreeNode<T>> children;

    public void addNode(TreeNode tree) {
        if (Objects.isNull(children)) {
            children = Lists.newArrayList();
        }
        children.add(tree);
    }


    public static void main(String[] args) throws JsonProcessingException {
        TreeNode<Object> root = TreeNode.builder()
                .data("0")
                .build();

        TreeNode treeNode1 = new TreeNode();
        treeNode1.setData("1");

        TreeNode treeNode2 = new TreeNode();
        treeNode2.setData("2");

        root.addNode(treeNode1);
        root.addNode(treeNode2);

        TreeNode treeNode3 = new TreeNode();
        treeNode3.setData("3");

        TreeNode treeNode4 = new TreeNode();
        treeNode4.setData("4");

        treeNode1.addNode(treeNode3);
        treeNode1.addNode(treeNode4);

        TreeNode treeNode5 = new TreeNode();
        treeNode5.setData("5");

        TreeNode treeNode6 = new TreeNode();
        treeNode6.setData("6");

        treeNode2.addNode(treeNode5);
        treeNode2.addNode(treeNode6);

//        log.info("{}", root);

        PreVisit preVisit = new PreVisit();
        preVisit.visit2(root);
//
//        preVist.visit(root);

//        DepthVisit depthVisit = new DepthVisit();
//        depthVisit.visit(root);


    }


    public static <T extends Serializable> T clone(T object) throws IOException,
            ClassNotFoundException {
        // 说明：调用ByteArrayOutputStream或ByteArrayInputStream对象的close方法没有任何意义
        // 这两个基于内存的流只要垃圾回收器清理对象就能够释放资源，这一点不同于对外资源(如文件流)的释放
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (T) ois.readObject();
    }

    // 求树的深度
    public int dept() {
        return dept(this);
    }

    // 判断是否为空树
    public boolean isEmpty() {
        if (CollectionUtils.isEmpty(children) && data == null)
            return true;
        return false;
    }

    // 判断是否为叶子节点
    public boolean isLeaf() {
        if (children.isEmpty())
            return true;
        return false;
    }

    private int dept(TreeNode tree) {
        if (tree.isEmpty()) {
            return 0;
        } else if (tree.isLeaf()) {
            return 1;
        } else {
            int n = children.size();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                if (children.get(i).isEmpty()) {
                    a[i] = 0 + 1;
                } else {
                    a[i] = dept(children.get(i)) + 1;
                }
            }
            Arrays.sort(a);
            return a[n - 1];
        }
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public T getRootData() {
        return data;
    }


}
