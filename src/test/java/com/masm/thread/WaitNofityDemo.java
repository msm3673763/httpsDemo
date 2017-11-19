package com.masm.thread;

/**
 * Created by masiming on 2017/10/16.
 * 下面有一段代码，假设两个方法分别处于两个线程中，但是用的是同一个锁lock，如果在线程1中调用testMethod()方法，线程2中调用synNotifyMethod()方法，
 * 那么在线程2中synNotifyMethod调用完lock.notify()之后，那么能肯定先输出1或者先输出2吗？请说出结果并说明原因
 * 输出：
 *  begin wait() ThreadName=Thread-0
 begin notify() ThreadName=Thread-1time=1508146007355
 2
 1
 *  说明：
 *  当执行notify/notifyAll方法时，会唤醒一个处于等待该对象锁的线程，然后继续往下执行，直到执行完退出对象锁锁住的区域（synchronized修饰的代码块）后再释放锁
 */
public class WaitNofityDemo {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin wait() ThreadName=" + Thread.currentThread().getName());
                lock.wait();
                System.out.println("1");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void synNotifyMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin notify() ThreadName=" + Thread.currentThread().getName()
                        + "time=" + System.currentTimeMillis());
                lock.notify();
                Thread.sleep(5000);
                System.out.println("2");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WaitNofityDemo demo = new WaitNofityDemo();
        Thread t1 = new Thread(() -> demo.testMethod(WaitNofityDemo.class));
        Thread t2 = new Thread(() -> demo.synNotifyMethod(WaitNofityDemo.class));
        t1.start();
        t2.start();
    }
}
