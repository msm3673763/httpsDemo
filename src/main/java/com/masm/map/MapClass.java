package com.masm.map;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/21

 * Contributors:
 *      - initial implementation
 */

import java.util.LinkedList;

/**
 * HashMap的简单实现
 *
 * @author ucs_masiming
 * @since 2017/8/21
 */
public class MapClass {

    LinkedList[] arr = new LinkedList[100];

    public void put(Object key, Object value) {
        System.out.println(key.hashCode());
        MyEntry entry = new MyEntry(key, value);
        int a = key.hashCode() % arr.length;
        if (arr[a] == null) {
            LinkedList linkedList = new LinkedList();
            linkedList.add(entry);
            arr[a] = linkedList;
        } else {
            for (Object obj : arr[a]) {
                MyEntry myEntry = (MyEntry) obj;
                if (key.equals(myEntry.getKey())) {
                    myEntry.value = value;
                    return;
                }
            }
            arr[a].add(entry);
        }
    }

    public Object get(Object key) {
        int a = key.hashCode() % arr.length;
        if (arr[a] != null) {
            for (Object obj : arr[a]) {
                MyEntry entry = (MyEntry) obj;
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    private class MyEntry {

        private Object key;
        private Object value;

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public MyEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }


    public static void main(String[] args) {
        MapClass map = new MapClass();
        map.put("name", "123");
        map.put("age", "23");
        map.put("age", "26");
        String age = (String) map.get("age");
        System.out.println(age);

    }
}
