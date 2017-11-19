package com.masm.cache.task;

import com.masm.cache.service.OrderService;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2017/11/11.
 */
public class OrderTask implements Runnable {

    CountDownLatch latch;
    OrderService orderService;
    InterProcessMutex interProcessMutex;

    public OrderTask(CountDownLatch latch, OrderService orderService) {
        this.latch = latch;
        this.orderService = orderService;
    }

    public OrderTask(CountDownLatch latch, OrderService orderService, InterProcessMutex interProcessMutex) {
        this.latch = latch;
        this.orderService = orderService;
        this.interProcessMutex = interProcessMutex;
    }

    @Override
    public void run() {
        try {
            latch.await();
            interProcessMutex.acquire();
            System.out.println(Thread.currentThread().getName() + ",订单号：" + orderService.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                interProcessMutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
