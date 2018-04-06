package com.masm.cache.lock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;

/**
 * curator实现分布式锁
 *
 * @author masiming
 * @create 2017/12/09
 **/
public class CuratorLock {

    private static final String CONNECT_ADDR = "192.168.31.128:2181,192.168.31.129:2181,192.168.31.130:2181";
    //session超时时间 单位ms
    private static final int SESSION_TIMEOUT = 10000;

    public static CuratorFramework createCuratorFramework() {
        //1、重试策略：初试时间为1s，重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

        //2、通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
                //连接超时时间，默认为15000ms
                .connectionTimeoutMs(10000)
                //会话超时时间，默认为60000毫秒
                .sessionTimeoutMs(SESSION_TIMEOUT)
                //有四种重试策略：ExponentialBackoffRetry、RetryNTimes、RetryOneTimes、RetryUntilElapsed
                .retryPolicy(retryPolicy)
                .build();
        return cf;
    }

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i=0;i<10;i++) {
            new Thread(() -> {
                CuratorFramework cf = createCuratorFramework();
                cf.start();
                final InterProcessMutex lock = new InterProcessMutex(cf, "/lock");
                try {
                    latch.await();
                    lock.acquire();
                    System.out.println(Thread.currentThread().getName() + "执行业务逻辑....");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                        cf.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "t" + i).start();
        }
        Thread.sleep(2000);
        latch.countDown();
    }
}
