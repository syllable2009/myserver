package com.neo.datastructure.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
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
public class BinaryTree<T> implements Serializable {

    private T data;
    BinaryTree<T> left;
    BinaryTree<T> right;

    public boolean isEmpty() {
        if (Objects.isNull(data) && Objects.isNull(left) && Objects.isNull(right)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("AlibabaAvoidApacheBeanUtilsCopy")
    public static void main(String[] args) throws Exception {

//        BinaryTree<Object> test1 = BinaryTree.builder()
//                .data("test1")
//                .build();
//        TreeNode<Object> test2 = TreeNode.builder()
//                .build();
//        BeanUtils.copyProperties(test2, test1); // 无法完成子属性的赋值
//        BinaryTree<Object> clone2 = BinaryTree.clone(test1);
//        log.info("{}", clone2);

        BinaryTree<String> root = new BinaryTree<>();
        root.setData("0");

        BinaryTree b1 = BinaryTree.builder().data("1").build();
        BinaryTree b2 = BinaryTree.builder().data("2").build();

        BinaryTree b3 = BinaryTree.builder().data("3").build();
        BinaryTree b4 = BinaryTree.builder().data("4").build();
        b1.setLeft(b3);
        b1.setRight(b4);

        BinaryTree b5 = BinaryTree.builder().data("5").build();
        BinaryTree b6 = BinaryTree.builder().data("6").build();
        b2.setLeft(b5);
        b2.setRight(b6);

        root.setLeft(b1);
        root.setRight(b2);


//        BinaryVisit.midVisit(root); //3140526
//        BinaryVisit.leftVisit(root); //0134256
//        BinaryVisit.rightVisit(root); //3415620
//        BinaryVisit.levelVisit(root); //0123456
        BinaryVisit.depthVisit(root);
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


}
