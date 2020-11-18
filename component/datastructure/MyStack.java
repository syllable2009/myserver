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
public class MyStack<T> {

    private Node<T> head;
    private int size;//栈的元素个数

    private static class Node<T> {
        private Node<T> next;
        private T data;

        public Node(T data) {
            this.data = data;
        }

    }


    public static void main(String[] args) throws Exception {
        MyStack<String> stack = new MyStack();
        log.info("stack1:{}", stack);
        stack.push("1");
        stack.push("2");
        stack.push("3");
        log.info("stack2:{}", stack);
        int size = stack.size;
        for (int i = 0; i < size; i++) {
            log.info("i:{}-{}", i, stack.pop());
        }
    }


    public T pop() {
        T t = head.data;
        if (head.next != null) {
            head = head.next;
            size--;
        }
        return t;

    }

    public void push(T data) {
        Node<T> node = new Node(data);
        node.next = head;
        head = node;
        size++;
    }
}
