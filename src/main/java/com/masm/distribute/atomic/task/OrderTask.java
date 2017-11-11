package com.masm.distribute.atomic.task;

import com.masm.distribute.atomic.service.OrderService;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2017/11/11.
 */
public class OrderTask implements Runnable {

    CountDownLatch latch;
    OrderService orderService;

    public OrderTask(CountDownLatch latch, OrderService orderService) {
        this.latch = latch;
        this.orderService = orderService;
    }

    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ",订单号：" + orderService.getOrderId());
    }
}
