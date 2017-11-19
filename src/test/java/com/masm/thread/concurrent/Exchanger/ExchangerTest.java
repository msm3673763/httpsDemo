package com.masm.thread.concurrent.Exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by masiming on 2017/11/12 23:13.
 * Exchanger：两个线程进行数据交换
 */
public class ExchangerTest {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();//交换器，交换String类型数据
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 10,
                2, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2));
        executor.execute(() -> {//张三团伙，把大乔发送出去，交换回money
            try {
                String content = exchanger.exchange("大乔");
                System.out.println("张三团伙用大乔交换回：" + content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executor.execute(() -> {//孙策，把1000W发送出去，交换回大乔
            try {
                String content = exchanger.exchange("1000W");
                System.out.println("孙策用1000W交换回：" + content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }
}
