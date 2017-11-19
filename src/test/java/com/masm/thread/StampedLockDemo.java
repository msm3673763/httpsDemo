package com.masm.thread;

import java.time.Instant;
import java.util.concurrent.locks.StampedLock;

/**
 * Created by masiming on 2017/10/20.
 */
public class StampedLockDemo {

    private double x,y;
    private final StampedLock sl = new StampedLock();

    public void move(double deltaX, double deltaY) {//an exclusively locked method
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    public double distanceFromOrigin() {//A read-only method
        long stamp = sl.tryOptimisticRead();
        double currentX = x, currentY = y;
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    public static void main(String[] args) {
        Instant instant = Instant.now();
        System.out.println(instant);


    }
}
