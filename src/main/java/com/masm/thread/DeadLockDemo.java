package com.masm.thread;

/**
 * Created by Administrator on 2017/10/6.
 * 死锁演示代码
 */
public class DeadLockDemo extends Thread {

    public static void main(String[] args) {
        DeadLockRunnable r1 = new DeadLockRunnable(true);
        DeadLockRunnable r2 = new DeadLockRunnable(false);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }
}

class DeadLockRunnable implements Runnable {

    private boolean flag;
    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public DeadLockRunnable(boolean flag) {
        this.flag = flag;
    }

    public void run() {
        if (flag) {
            synchronized (lockA) {
                System.out.println(Thread.currentThread().getName() +":" + flag);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lockB) {
                    System.out.println(Thread.currentThread().getName() + "获得锁LockB");
                }
            }
        } else {
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() +":" + flag);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lockA) {
                    System.out.println(Thread.currentThread().getName() + "获得锁LockA");
                }
            }
        }
    }
}