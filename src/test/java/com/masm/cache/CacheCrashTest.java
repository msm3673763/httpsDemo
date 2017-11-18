package com.masm.cache;

import com.masm.DemoApplication;
import com.masm.cache.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Created by masiming on 2017/11/18 17:29.
 * 使用CountDownLatch模拟并发测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class CacheCrashTest {

    @Autowired
    private OrderDetailService orderDetailService;

    private static final String USER_CODE = "u001";
    private static final int threadSize = 200;
    private CountDownLatch latch = new CountDownLatch(threadSize);

    /**
     * 测试前准备工作，将数据放在缓存中
     */
    @Before
    public void before() {
        orderDetailService.getOrderAmount(USER_CODE);
    }

    /**
     * 主测试方法，实例化threadSize个线程，并发执行查询操作
     */
    @Test
    public void crashTest() {
        for (int i=0;i<threadSize;i++) {
            new Thread(new UserRequest()).start();
            //倒计时器计数一次
            latch.countDown();
        }

        try {
            //阻塞主线程，等待所有子线程运行完毕
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class UserRequest implements Runnable {

        @Override
        public void run() {
            try {
                latch.await();//已实例化好的线程在此等待，当所有线程实例化完成后，同时停止等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            BigDecimal amount = orderDetailService.getOrderAmount(USER_CODE);//使用数据库中存在的用户编号查询
            BigDecimal amount = orderDetailService.getOrderAmount(UUID.randomUUID().toString()); //使用随机生成的用户编号查询,会造成缓存击穿
            log.info(Thread.currentThread().getName() + "--------->" + amount);
        }
    }
}
