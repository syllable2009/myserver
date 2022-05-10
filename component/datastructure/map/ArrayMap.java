package com.neo.datastructure.map;

import lombok.extern.slf4j.Slf4j;

import java.util.Map.Entry;

import static java.util.Objects.hash;

/**
 *
 **/
@Slf4j
public class ArrayMap<K, V> implements MyMap<K, V> {

    private int size;

    // 先不考虑扩容和阀值
    private MyEntry<K, V>[] table = null;


    public ArrayMap() {
        table = new MyEntry[16];
    }

    public ArrayMap(int initSize) {
        table = new MyEntry[initSize];
    }

    @Override
    public V get(K k) {
        // 通过hash散列
        int index = hash(k) % table.length;
        MyEntry<K, V> entry = table[index];
        if (null != entry) {
            while (null != entry) {
                if (k.equals(entry.getKey())) {
                    return entry.getValue();
                }
                entry = entry.next;
            }
        }
        return null;
    }

    @Override
    public V put(K k, V v) {
        // 通过hash散列
        int index = hash(k) % table.length;
        MyEntry<K, V> entry = table[index];
        if (null != entry) {
            V value = null;
            // 遍历链表是否有相等key, 有则替换且返回旧值
            while (entry != null) {
                if (entry.getKey().equals(k)) {
                    value = entry.getValue();
                    entry.setValue(v);
                    return value;
                }
                entry = entry.next;
            }
            // 没有则使用头插法
            table[index] = new MyEntry(k, v, table[0]);
            size++;
        } else {
            table[index] = new MyEntry(k, v, null);
            size++;
        }
        return null;
    }


    class MyEntry<K, V> implements Entry<K, V> {

        private K key;
        private V valve;
        private MyEntry<K, V> next;

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return valve;
        }

        @Override
        public V setValue(V value) {
            this.valve = value;
            return value;
        }

        public MyEntry(K key, V valve, MyEntry<K, V> next) {
            this.key = key;
            this.valve = valve;
            this.next = next;
        }
    }


    public static void main(String[] args) {
        ArrayMap<String, Integer> arrayMap = new ArrayMap();
        Integer a = arrayMap.put("A", 40);
        log.info("{},{}", a, arrayMap.size);
        a = arrayMap.put("A", 30);
        log.info("{},{}", a, arrayMap.size);
        a = arrayMap.put("B", 40);
        log.info("{},{}", a, arrayMap.size);
        a = arrayMap.get("A");
        log.info("{},{}", a, arrayMap.size);
        //        log.info("{}", hash("345") % 18);
        //        log.info("{}", hash("345") & (18 - 1));

     
    }
}
