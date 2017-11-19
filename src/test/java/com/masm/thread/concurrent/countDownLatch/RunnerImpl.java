package com.masm.thread.concurrent.countDownLatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by masiming on 2017/11/12 15:04.
 */
public class RunnerImpl {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int athlete = 8;//运动员数量
        CountDownLatch begin = new CountDownLatch(1);//裁判
        CountDownLatch end = new CountDownLatch(athlete);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10,
                2, TimeUnit.SECONDS, new ArrayBlockingQueue(3));
        List<Future<Integer>> list = new ArrayList<>();
        for (int i=0;i<athlete;i++) {
            list.add(executor.submit(new Runner(begin, end)));
        }
        begin.countDown();//裁判吹起开始哨声
        end.await();//等待所有的运动员跑完
        int count = 0, height = 0;
        for (Future<Integer> future : list) {
            count += future.get();
            if (future.get() > height) {
                height = future.get();
            }
        }
        System.out.println("运动员人数：" + athlete + ",总分：" + count + ",最高分：" + height);
        executor.shutdown();
    }
}
