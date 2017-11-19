package com.masm.collection;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by masiming on 2017/10/17.
 * 寻找数组相同元素
 */
public class ArrayTest {

    /**
     * 0~99共100个元素各不相同，新加入一个0~99的元素,不明确位置
     * 从101个元素数组中找出与0~99元素中重复的一个
     */
    @Test
    public void test1() {
        int[] arr = new int[101];
        for (int i=0;i<100;i++) {
            arr[i] = i;
        }

        arr[100] = 38;//假定重复元素为38

        //打乱
        for (int i=0;i<1000;i++) {//进行1000次数据打乱操作
            int index1 = ThreadLocalRandom.current().nextInt(99);
            int index2 = ThreadLocalRandom.current().nextInt(99);
            //交换元素
            int temp = arr[index1];
            arr[index1] = arr[index2];
            arr[index2] = temp;
        }
        for (int i : arr) {
            System.out.println(i + ",");
        }

        //使用异或解决
        for (int n=1;n<arr.length;n++) {
            arr[0] = arr[0]^arr[n];
        }

        for (int n=0;n<100;n++) {
            arr[0] = arr[0]^n;
        }

        System.out.println("重复为：" + arr[0]);
    }

    /**
     * 0~99共100个整数，各不相同，将所有数放入一个数组，随机排布，数组长度100，
     * 将其中任意一个数替换成0~99另一个数（唯一重复的数字）
     * 将重复的数字找出
     */
    @Test
    public void test2() {
        int arr[] = new int[100];
        for (int i=0;i<100;i++) {
            arr[i] = i;
        }

        //随机排布
        for (int i=0;i<1000;i++) {
            int index1 = ThreadLocalRandom.current().nextInt(100);
            int index2 = ThreadLocalRandom.current().nextInt(100);
            int temp = arr[index1];
            arr[index1] = arr[index2];
            arr[index2] = temp;
        }

        //用某个值给给某个值替换
        int idx1 = ThreadLocalRandom.current().nextInt(100);
        int idx2 = ThreadLocalRandom.current().nextInt(100);
        //保证idx1与idx2不同
        while (idx1 == idx2) {
            idx2 = ThreadLocalRandom.current().nextInt(100);
        }
        System.out.println("将" + idx1 + "位置的值用" + idx2 + "的位置替换");
        arr[idx1] = arr[idx2];

        for (int i=0;i<arr.length;i++) {
            System.out.print(arr[i] + "\t");
        }

        //用数组优化的算法：定义一个新数组int newArr[] = new int[100],默认值为0
        //把原始数组的元素作为新数组的下标，如果该下标对应的新数组元素存在，就将该元素值+1
        /*
		 * 原始数组         8 3 7 2 1 5 6 8 0
		 * 新数组           0 0 0 0 0 0 0 0 0
		 * 对新数组+1       1 1 1 1 0 1 1 1 2
		 */
        // 新数组中元素为2的值的下标就是重复元素
        int newArr[] = new int[100];
        for (int i=0;i<arr.length;i++) {
            newArr[arr[i]]++;
            if (newArr[arr[i]] == 2) {
                System.out.println("重复元素是：" + arr[i]);
            }
        }
    }

}
