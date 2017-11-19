package com.masm.thread.concurrent.semaphore;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/11/12 20:16.
 * 业务需求：
 *      假如现在有20个人去售票厅买票，但是窗口只有2个，那么同时能够
 * 买票的只能有2个人，当2个人中任意一个人买好票离开之后，等价的18个
 * 人中又会有一个人可以占用窗口买票
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20,
                2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5));
        for (int i=0;i<20;i++) {
            executor.execute(new MyRunnable(semaphore, i));
        }
        executor.shutdown();
    }
}
