package com.masm.designPattern;

/**
 * Created by Administrator on 2017/10/6.
 */
public class EnumSingleton {

    private EnumSingleton() {}

    public static EnumSingleton getInstance() {
        return SingleEnum.INSTANCE.getInstance();
    }

    private enum SingleEnum {
        INSTANCE;

        private EnumSingleton single;

        SingleEnum() {
            single = new EnumSingleton();
        }

        public EnumSingleton getInstance() {
            return single;
        }
    }

    public static void main(String[] args) {
        EnumSingleton single = EnumSingleton.getInstance();
        EnumSingleton single1 = EnumSingleton.getInstance();
        EnumSingleton single2 = EnumSingleton.getInstance();
        System.out.println(single == single1);
        System.out.println(single1 == single2);
        System.out.println(single == single2);
    }
}
