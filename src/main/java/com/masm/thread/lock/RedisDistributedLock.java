package com.masm.thread.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/10/23.
 * 工具类：基于Redisson实现分布式锁
 */
@Slf4j
public class RedisDistributedLock implements DistributedLock {

    private RLock rLock;

    public static final int INFINITE_LEASE_TIME = -1;

    public RedisDistributedLock(RLock rLock) {
        this.rLock = rLock;
    }

    @Override
    public void lock(long waitTime, TimeUnit timeUnit) {
        lock(waitTime, INFINITE_LEASE_TIME, timeUnit);
    }

    @Override
    public void lock(long waitTime, long leaseTime, TimeUnit timeUnit) {
        boolean result;
        try {
            result = rLock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            result = false;
            Thread.currentThread().interrupt();
        } catch (Exception e){
            throw new LockException(e.getMessage(), e);
        }
        if(!result){
            throw new LockException("can't require the lock resource in " + timeUnit.toMillis(waitTime) + " millisecond(s).");
        }
    }

    @Override
    public void unLock() {
        try {
            rLock.unlock();
        } catch (IllegalMonitorStateException e) {//NOSONAR
            // unLock()的时候有可能leaseTime到了锁释放了，所以需要catch IllegalMonitorStateException
            // 注意：正常的情况下，典型的内存锁实现下，不能只捕捉此异常而不做任何处理
            log.warn("unlock fail : "+ e.getMessage());
        }
    }

    @Override
    public boolean isLock() {
        return rLock.isExists() && rLock.isLocked();
    }

    @Override
    public boolean isHeldByCurrentThread() {
        return rLock.isHeldByCurrentThread();
    }
}
