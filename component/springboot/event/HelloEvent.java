package com.neo.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * 2020-11-24 18:52
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelloEvent {
    private String eventName;
}
