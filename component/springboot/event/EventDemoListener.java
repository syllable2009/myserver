package com.neo.event;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author jiaxiaopeng
 * 2020-11-04 16:41
 **/
// 定义一个事件监听者
@Component
public class EventDemoListener implements ApplicationListener<EventDemo> {

    @Async
    @Override
    public void onApplicationEvent(EventDemo event) {
        System.out.println(LocalDateTime.now().toString() + "收到事件：" + event.getMessage());
    }
}
