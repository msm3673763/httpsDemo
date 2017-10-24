package com.masm.thread.lock;

/**
 * Created by masiming on 2017/10/23.
 * 锁异常基类
 */
public class LockException extends RuntimeException {

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Exception e) {
        super(message, e);
    }
}
