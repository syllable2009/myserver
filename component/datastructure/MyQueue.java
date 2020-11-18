package com.neo.datastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-11-18 17:42
 **/
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyQueue<T> {

    private Node<T> head;//头结点
    private Node<T> tail;//尾结点
    private int size;//元素个数

    private static class Node<T> {
        private Node<T> next;
        private T data;

        public Node(T data) {
            this.data = data;
        }

    }


    public static void main(String[] args) throws Exception {
        MyQueue<String> queue = new MyQueue();
        log.info("queue1:{}", queue);
        queue.offer("1");
        queue.offer("2");
        queue.offer("3");
        log.info("queue2:{}", queue);
        int size = queue.size;
        for (int i = 0; i < size; i++) {
            log.info("i:{}-{}", i, queue.poll());
        }
    }


    public T poll() {
        T t = head.data;
        if (head.next != null) {
            head = head.next;
        }
        size--;
        return t;

    }

    public void offer(T data) {
        Node<T> node = new Node(data);
        node.next = null;
        if (size == 0) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }
}
