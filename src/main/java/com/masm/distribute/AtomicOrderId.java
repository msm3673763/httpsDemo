package com.masm.distribute;

import com.masm.distribute.service.OrderService;
import com.masm.distribute.service.impl.AtomicOrderServiceImpl;
import com.masm.distribute.task.OrderTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/11/11.
 * 根据原子性生成id（不是分布式）
 */
public class AtomicOrderId {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new ArrayBlockingQueue(2));
        final CountDownLatch latch = new CountDownLatch(1);
        OrderService orderService = new AtomicOrderServiceImpl();
        for (int i=0;i<10;i++) {
            executor.execute(new OrderTask(latch, orderService));
        }
        latch.countDown();
        executor.shutdown();
    }
}
