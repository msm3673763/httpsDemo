package com.masm.thread;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/23

 * Contributors:
 *      - initial implementation
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 暂无描述
 *
 * @author ucs_masiming
 * @since 2017/8/23
 */
public class ThreadPoolDemo {

    private static int corePoolSize = 5;
    private static int maxPoolSize = 10;
    private static long keepAliveTime = 3;

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.DiscardPolicy());
    }
}
