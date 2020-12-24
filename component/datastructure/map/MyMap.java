package com.neo.datastructure.map;

/**
 * @author jiaxiaopeng
 * 2020-12-24 15:12
 **/
public interface MyMap<K, V> {

    V get(K k);

    V put(K k, V v);
}
