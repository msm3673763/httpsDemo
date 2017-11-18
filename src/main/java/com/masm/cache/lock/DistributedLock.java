package com.masm.cache.lock;

import com.masm.cache.lock.redission.LockException;

import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/10/23.
 * 分布式锁接口
 */
public interface DistributedLock {

    /**
     * 加锁，在指定时间内获取锁失败会抛出
     * @param waitTime 最长等待锁时间
     * @param timeUnit 时间单位
     * @exception LockException 加锁失败时抛出异常
     */
    void lock(long waitTime, TimeUnit timeUnit);

    /**
     * 加锁
     * @param waitTime 最长等待锁时间
     * @param leaseTime 持有锁最长时间
     * @param timeUnit 时间单位
     * @exception LockException 加锁失败时抛出异常
     */
    void lock(long waitTime, long leaseTime, TimeUnit timeUnit);

    /**
     * 解锁
     */
    void unLock();

    /**
     * 判断锁对象是否被人持有
     */
    boolean isLock();

    /**
     * 判断是否被当前线程持有
     * @return boolean
     */
    boolean isHeldByCurrentThread();
}
