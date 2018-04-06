package com.masm.cache.lock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * 分布式计数器：
 *      一说到分布式计数器，你可能脑海里想到了AtomicInteger这种经典的方式，如果针对于一个jvm的场景当然没问题，
 *      但是我们现在是分布式场景下，就需要利用Curator框架的DistributedAtomicInteger了
 * @author masiming
 * @create 2017/12/09
 **/
public class CuratorAutomicInteger {

    private static final String CONNECT_ADDR = "192.168.31.128:2181,192.168.31.129:2181,192.168.31.130:2181";
    //session超时时间 单位ms
    private static final int SESSION_TIMEOUT = 10000;

    public static void main(String[] args) throws Exception {
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
        cf.start();

        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(
                cf, "/atomicInteger", new RetryNTimes(3, 1000));
        //重置
        atomicInteger.forceSet(0);
        atomicInteger.increment();
        AtomicValue<Integer> value = atomicInteger.get();
        System.out.println(value.succeeded());
        //最新值
        System.out.println(value.postValue());
        //原给值
        System.out.println(value.preValue());
    }
}
