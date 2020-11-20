package com.neo.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author jiaxiaopeng
 * 2020-11-04 16:37
 **/

// 定义一个事件
public class EventDemo<T> extends ApplicationEvent {

    private T data;

    public EventDemo(Object source, T data) {
        super(source);
        this.data = data;
    }

    public T getMessage() {
        return data;
    }
}
