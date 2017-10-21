package com.masm.jdk8;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Administrator on 2017/10/15.
 */
public class LambdaDemo {

    public static void main(String[] args) {
        //Lambda语法
        //语法一：无参数，无返回值
        Runnable r = () -> System.out.println("Hello Lambda!");
        r.run();

        //语法二：有一个参数，无返回值
        Consumer<String> consumer = (x) -> System.out.println(x);
        consumer.accept("Hello Lambda!");

        //语法三：只有一个参数，小括号可以省略不写
        consumer = x -> System.out.println(x);
        consumer.accept("Hello Lambda!");

        //语法四：有两个以上的参数，有返回值，并且Lambda体中有多条语句
        Comparator<Integer> comparator = (x, y) -> {
            System.out.println("Hello Lambda!");
            return Integer.compare(x, y);
        };
        int i = comparator.compare(2423, 2422);
        System.out.println(i);

        //语法五：若Lambda体中只有一条语句，return和大括号都可以省略不写
        comparator = (x, y) -> Integer.compare(x, y);
        comparator.compare(2322, 3412);

        //语法六：Lambda表达式的参数列表的数据类型可以省略不写，因为JVM编译器通过上下文推断出数据类型，即“类型推断”
        comparator = (Integer x, Integer y) -> Integer.compare(x, y); //Integer可以不写
        comparator.compare(2322, 3412);

        //方法引用 ①对象::实例方法名
        Consumer<String> c = System.out::println;//等于Consumer<Stirng> c = x -> System.out.println(x);
        c.accept("Hello Lambda!");

        //方法引用 ②类::静态方法名
        Comparator<Integer> com = Integer::compare;//等于Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
        com.compare(2321, 2314);

        //方法引用 ③类::实例方法名
        BiPredicate<String, String> bp = String::equals;//等于BiPredicate<String, String> bp = (x, y) -> x.equals(y);
        boolean result = bp.test("dfdsf", "dfdsf");
        System.out.println(result);

        //数组引用
        Function<Integer, String[]> function = String[]::new;
        String[] strArr = function.apply(10);
        System.out.println(strArr.length);
//        Function<Integer, String[]> function = x -> new String[x];
//        String[] strArr = function.apply(10);
//        System.out.println(strArr.length);
    }
}
