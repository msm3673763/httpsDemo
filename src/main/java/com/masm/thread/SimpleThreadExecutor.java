package com.masm.thread;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/9/8

 * Contributors:
 *      - initial implementation
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 暂无描述
 *
 * @author ucs_masiming
 * @since 2017/9/8
 */
public class SimpleThreadExecutor implements Executor {

    private BlockingQueue<Runnable> taskQueue = null;//任务队列
    private List<WorkerThread> threads = new ArrayList();//线程池
    private boolean isStopped = false;

    public SimpleThreadExecutor(int coreSize, int maxSize, int blockQueueSize) {
        this.taskQueue = new ArrayBlockingQueue<Runnable>(blockQueueSize);
        for (int i=0;i<coreSize;i++) {
            threads.add(new WorkerThread(taskQueue));
        }
        for (WorkerThread thread : threads) {
            thread.start();
        }
    }

    public synchronized void execute(Runnable runnable) {
        if (this.isStopped) {
            throw new IllegalStateException("SimpleThreadPoolExecutor is stopped" );
        }
        this.taskQueue.add(runnable);
    }

    public synchronized void shutdown() {
        this.isStopped = true;
        for (WorkerThread thread : threads) {
            thread.toStop();//循环中断每一个线程
        }
    }
}

class WorkerThread extends Thread {
    private BlockingQueue<Runnable> taskQueue = null;
    private boolean isStopped = false;

    public WorkerThread(BlockingQueue<Runnable> queue) {
        this.taskQueue = queue;
    }

    @Override
    public void run() {
        //因为需要不断从的任务列出中取出task执行，因此需要放在一个循环中，否则线程对象执行完一个任务就会立刻结束
        while (!isStopped()) {
            try {
                Runnable runnable = taskQueue.take();
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void toStop() {
        isStopped = true;
        this.interrupt();//如果线程正在任务队列中获取任务，或者没有任务被阻塞，需要响应这个中断
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

}
