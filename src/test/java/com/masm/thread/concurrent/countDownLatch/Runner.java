package com.masm.thread.concurrent.countDownLatch;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/11/12 14:56.
 */
public class Runner implements Callable<Integer> {

    private CountDownLatch begin;
    private CountDownLatch end;

    public Runner(CountDownLatch begin, CountDownLatch end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public Integer call() throws Exception {
        begin.await();//裁判等待运动员上场
        int score = ThreadLocalRandom.current().nextInt(10);
        TimeUnit.MILLISECONDS.sleep(score);//运动员跑步需要花费的时间
        end.countDown();//运动员跑步完成
        return score;
    }
}
