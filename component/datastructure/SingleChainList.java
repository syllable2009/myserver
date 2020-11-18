package com.neo.datastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-11-17 10:11
 **/
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleChainList<T> {


    private Node<T> head;// 头部元素
    private int size;


    private static class Node<T> {
        private Node<T> next;//下一个结点
        private T data;//结点的数据

        public Node(T t) {
            data = t;
        }
    }

    //    在链表头部添加一个结点
    public void addFirst(T t) {
        Node<T> node = new Node(t);
        node.next = head;
        head = node;
        size++;
    }

    //    在链表头部添加一个结点
    public void addLast(T t) {
        Node node = new Node(t);
        node.next = null;
        if (size == 0) {
            head = node;
        } else {
            Node<T> last = head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = node;

        }
        size++;
    }


    public static void main(String[] args) throws Exception {

        SingleChainList<String> t = new SingleChainList<String>();
        t.addFirst("1");
        t.addFirst("2");
        t.addLast("3");
        t.addLast("4");

        Node i = t.head;
        log.info("{}", i.data);
        while (i.next != null) {
            i = i.next;
            log.info("{}", i.data);
        }
    }

    //    获取链表指定下标的结点
    public T get(int index) {
        if (index < 0 || index > size) {
            return null;
//            throw new IndexOutOfBoundsException("size:" + size + ",index:" + index);
        }
        if (index == 0) {
            return head.data;
        }
        Node<T> mid = head;
        int j = 0;
        while (j < index) {
            mid = mid.next;
            j++;
        }
        return mid.data;
    }
}
