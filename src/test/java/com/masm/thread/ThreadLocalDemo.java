package com.masm.thread;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Administrator on 2017/10/6.
 * 测试ThreadLocal（以空间换时间）
 * 本例输出结果为：
 *      t1:123456
 *      t2:null
 */
public class ThreadLocalDemo {
    private ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public void set(String value) {
        threadLocal.set(value);
    }

    public void get() {
        System.out.println(Thread.currentThread().getName() + ":" + this.threadLocal.get());
    }

    public static void main(String[] args) {
        final ThreadLocalDemo demo = new ThreadLocalDemo();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                demo.set("123456");
                demo.get();
            }
        }, "t1");

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    demo.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");
        t1.start();
        t2.start();
    }

}
