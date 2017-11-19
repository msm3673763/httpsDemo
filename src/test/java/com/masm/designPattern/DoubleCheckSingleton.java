package com.masm.designPattern;

/**
 * Created by Administrator on 2017/10/6.
 */
public class DoubleCheckSingleton {

    private DoubleCheckSingleton() {}

    private static volatile DoubleCheckSingleton singleton = null;

    public static DoubleCheckSingleton getInstance() {
        if (singleton == null) {
            synchronized (DoubleCheckSingleton.class) {
                if (singleton == null) {
                    singleton = new DoubleCheckSingleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        DoubleCheckSingleton instance1 = DoubleCheckSingleton.getInstance();
        DoubleCheckSingleton instance2 = DoubleCheckSingleton.getInstance();
        DoubleCheckSingleton instance3 = DoubleCheckSingleton.getInstance();
        System.out.println(instance1 == instance2);
        System.out.println(instance1 == instance3);
        System.out.println(instance2 == instance3);
    }

}
