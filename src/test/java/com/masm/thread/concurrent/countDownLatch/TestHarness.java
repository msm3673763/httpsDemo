package com.masm.thread.concurrent.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * Created by masiming on 2017/11/12 18:03.
 */
public class TestHarness {

    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i=0;i<nThreads;i++) {
            new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        long start = System.nanoTime();//获取当前纳秒数，纳秒通常用于性能测试 1毫秒=1000000纳秒
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end-start;
    }

    public static void main(String[] args) throws InterruptedException {
        TestHarness testHarness = new TestHarness();
        long time = testHarness.timeTasks(10, () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(time);
    }
}
