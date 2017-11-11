package com.masm.distribute.atomic;

import com.masm.distribute.atomic.service.OrderService;
import com.masm.distribute.atomic.service.impl.AtomicOrderServiceImpl;
import com.masm.distribute.atomic.task.OrderTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/11/11.
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
