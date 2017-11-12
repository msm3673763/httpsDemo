package com.masm.distribute;

import com.masm.distribute.service.OrderService;
import com.masm.distribute.service.impl.NoLockOrderServiceImpl;
import com.masm.distribute.task.OrderTask;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/11/11.
 * zookeeper分布式锁生成分布式全局id
 */
public class CuratorOrderId {

    static final CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.101:2181,192.168.1.102:2181,192.168.1.103:2181")
            .retryPolicy(new ExponentialBackoffRetry(100, 1)).build();

    public static void main(String[] args) {
        client.start();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 2, TimeUnit.SECONDS, new ArrayBlockingQueue(3));
        final CountDownLatch latch = new CountDownLatch(1);
        final InterProcessMutex lock = new InterProcessMutex(client, "/tl");
        OrderService orderService = new NoLockOrderServiceImpl();
        for (int i=0;i<10;i++) {
            executor.execute(new OrderTask(latch, orderService, lock));
        }
        latch.countDown();
        executor.shutdown();
    }
}
