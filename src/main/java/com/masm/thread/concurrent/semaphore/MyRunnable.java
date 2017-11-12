package com.masm.thread.concurrent.semaphore;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.concurrent.Semaphore;

/**
 * Created by masiming on 2017/11/12 20:17.
 */
public class MyRunnable implements Runnable {

    private Semaphore semaphore;
    private int user;

    public MyRunnable(Semaphore semaphore, int user) {
        this.semaphore = semaphore;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();//获取信号量（占用窗口）
            System.out.println("用户【" + user + "】进入窗口，准备买票");
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
            System.out.println("用户【" + user + "】买票完成，即将离开");
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
            System.out.println("用户【" + user + "】离开售票窗口");
            semaphore.release();//释放许可，并且返回给信号量
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
