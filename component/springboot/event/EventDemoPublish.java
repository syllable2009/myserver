package com.neo.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author jiaxiaopeng
 * 2020-11-04 16:43
 **/
// 事件发布
@Component
public class EventDemoPublish<T> {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(T data) {
        EventDemo demo = new EventDemo(this, data);
        applicationEventPublisher.publishEvent(demo);
    }

}
