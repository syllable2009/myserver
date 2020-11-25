package com.neo.event;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * 2020-11-24 18:53
 **/
@Data
@NoArgsConstructor
public class WorldEvent extends HelloEvent {

    private int eventNo;

    public WorldEvent(String name, int no) {
        setEventName(name);
        setEventNo(no);
    }
}
