package com.neo.event;

import com.google.common.eventbus.EventBus;

/**
 * @author jiaxiaopeng
 * 2020-11-24 18:55
 **/
public class GuavaEventTest {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        GeventListener listener = new GeventListener();
        eventBus.register(listener);

        eventBus.post(new HelloEvent("hello"));
        eventBus.post(new WorldEvent("world", 23333));
    }
}
