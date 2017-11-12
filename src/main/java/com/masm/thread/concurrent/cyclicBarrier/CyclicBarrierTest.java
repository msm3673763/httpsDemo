package com.masm.thread.concurrent.cyclicBarrier;

import java.util.concurrent.*;

/**
 * Created by masiming on 2017/11/12 22:00.
 * CyclicBarrier可以循环使用
 * 业务需求：
 *      公司周末组织聚餐，首先各自从家里到聚餐地点，全部到齐之后，
 * 才开始一起吃东西，如果人员没有到齐，到的人就只能等待在那里，直到
 * 全部到齐之后才能做后面的事情
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
//        CyclicBarrier barrier = new CyclicBarrier(3);
        //在全部人到齐之前各自可以进行一些任务
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("自由组队游玩");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 10,
                2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2));

        for (int i=0;i<3;i++) {
            final int user = i + 1;
            executor.execute(() -> {
                try {
                    //模拟每个人来的时间各不相同
                    Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
                    System.out.println(user + "到达，当前共有" + barrier.getNumberWaiting()+1 + "人在等待");
                    barrier.await();//阻塞点
                    System.out.println("12点，全部人一起去吃饭。。。。");
                    Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
                    System.out.println(user + "吃完饭了，分别回到家");
                    //继续到轰趴馆玩 cyclicBarrier可以继续使用 barrier。await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }
}


