package com.masm.cache.lock.redission;

/**
 * 锁异常基类
 *
 * @author masiming
 * @create 2017/10/23
 **/
public class LockException extends RuntimeException {

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Exception e) {
        super(message, e);
    }
}
