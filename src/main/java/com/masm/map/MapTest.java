package com.masm.map;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by masiming on 2017/11/10.
 */
public class MapTest {

    private Map<Integer, Integer> map;

    //创建一个空的Map
    private Map<Integer, Integer> emtryMap1 = Collections.emptyMap();
    private Map<Integer, Integer> emtryMap2 = new HashMap();

    //初始化一个不可变Map：正确做法
    private static Map<Integer, Integer> unmodifiableMap = new HashMap<>();
    static {
        unmodifiableMap.put(1, 10);
        unmodifiableMap.put(2, 20);
        unmodifiableMap = Collections.unmodifiableMap(unmodifiableMap);
    }

    /**
     * 初始化一个不可变Map：错误做法
     * 加了final只能确保不能map1 = new，但是可以修改map1中的元素
     */
    private static final Map<Integer, Integer> map1 = new HashMap<>();
    static {
        map1.put(1, 10);
        map1.put(2, 20);
    }

    @Before
    public void before() {
        map = new HashMap<>();
        map.put(21,10);
        map.put(13,30);
        map.put(34,20);
    }

    /**
     * 将Map转化成List
     * Map接口提供了三种collection：key set，value set和key-value set，每一种都可以转成List
     */
    @Test
    public void testMapToList() {
        //key list
        ArrayList<Integer> keyList = new ArrayList<>(map.keySet());
        System.out.println(keyList.toString());
        //value list
        ArrayList<Integer> valueList = new ArrayList<>(map.values());
        System.out.println(valueList.toString());
        //key-value list
        ArrayList<Map.Entry<Integer,Integer>> entryList = new ArrayList<>(map.entrySet());
        System.out.println(entryList.toString());
    }

    /**
     * 迭代map
     */
    @Test
    public void testIteratorMap() {
        //第一种 增强for循环
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            System.out.println("key:" + key + ",value:" + value);
        }

        //第二种 iterator,jdk1.5之前
        Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            int key = entry.getKey();
            int value = entry.getValue();
            System.out.println("key:" + key + ",value:" + value);
        }
    }

    /**
     * 根据key对map进行排序
     */
    @Test
    public void testKeySortMap() {
        ArrayList<Map.Entry<Integer,Integer>> list = new ArrayList<>(map.entrySet());
        list.stream().sorted(Comparator.comparing(Map.Entry::getKey));
        for (Map.Entry<Integer, Integer> entry : list) {
            int key = entry.getKey();
            int value = entry.getValue();
            System.out.println("key:" + key + ",value:" + value);
        }
    }

    /**
     * 根据value对map进行排序
     */
    @Test
    public void testValueSortMap() {
        ArrayList<Map.Entry<Integer,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue));
        for (Map.Entry<Integer, Integer> entry : list) {
            int key = entry.getKey();
            int value = entry.getValue();
            System.out.println("key:" + key + ",value:" + value);
        }
    }

}
