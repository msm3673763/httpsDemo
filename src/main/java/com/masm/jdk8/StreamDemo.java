package com.masm.jdk8;

import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 一、Stream的三个操作步骤
 * 1、创建Stream   2、中间操作  3、终止操作（中断操作）
 *
 *
 * Created by Administrator on 2017/10/15.
 *
 */
public class StreamDemo {

    List<Employee> employees = Arrays.asList(
            new Employee("张三", 18, 9999.99, Status.FREE.toString()),
            new Employee("李四", 58, 5555.55, Status.BUSY.toString()),
            new Employee("王五", 26, 3333.33, Status.VOCATION.toString()),
            new Employee("赵六", 36, 6666.66, Status.FREE.toString()),
            new Employee("田七", 12, 8888.88, Status.BUSY.toString()),
            new Employee("田七", 12, 8888.88, Status.BUSY.toString())
    );

    /**
     * 1、创建Stream
     */
    @Test
    public void test1() {
        //1、可以通过Collection系列集合提供的stream()或parallelStream()获取
        List<Integer> list = new ArrayList<>();
        Stream<Integer> stream = list.stream();//串行

        //2、通过Arrays中的静态方法stream()获取数组流
        Integer[] arr = new Integer[10];
        stream = Arrays.stream(arr);

        //3、通过Stream类中的静态方法of()
        stream = Stream.of(1, 2, 3);

        //4、创建无限流
        //迭代
        stream = Stream.iterate(0, x -> x + 2);
        stream.limit(10).forEach(System.out::println);

        //生成
        Stream<Integer> stream1 = Stream.generate(() -> ThreadLocalRandom.current().nextInt(100));
        stream1.limit(5).forEach(System.out::println);
    }

    /**
     * 2、中间操作：筛选与切片
     * filter---接收Lambda，从流中排除某些元素
     * limit----截断流，使其元素不超过给定数量
     * skip(n)--跳过元素，返回一个扔掉了前n个元素的流，若流中元素不足n个，则返回一个空流。与limit(n)互补。
     * distinct-筛选，通过流所生成元素的hashCode()和equals()去除重复元素
     */
    @Test
    public void test2() {
        //内部迭代：迭代操作由Stream API完成
        Stream<Employee> stream = employees.stream().filter(e -> {//filter操作
//            System.out.println("Stream API的中间操作");
            return e.getAge() > 35;
        });
        List<Employee> list = stream.collect(Collectors.toList());
//        stream.limit(1);//limit操作 有短路效果 && ||
//        stream.forEach(System.out::println);//终止操作：一次性执行全部内容，即“惰性求值”

        //外部迭代
        Iterator<Employee> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * 3、中间操作：映射
     * map      接收Lambda，将元素转换成其它形式或提取信息。接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
     * flatMap  接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
     */
    @Test
    public void test3() {
        //map：转换成其它形式
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");
        list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);

        //map：提取信息
        employees.stream().map(e -> e.getName()).forEach(System.out::println);

        Stream<Stream<Character>> stream = list.stream().map(StreamDemo::filterCharacter);
        stream.forEach(s -> {
            s.forEach(System.out::print);
        });

        System.out.println();

        //flatMap
        list.stream().flatMap(StreamDemo::filterCharacter).forEach(System.out::print);//相当于上段代码
    }

    public static Stream<Character> filterCharacter(String str) {
        List<Character> list = new ArrayList<>();

        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }

        return list.stream();
    }

    /**
     * 中间操作：排序
     * sorted（）     自然排序
     * sorted（Comparator com)   定制排序
     */
    @Test
    public void test4() {
        //自然排序
        List<String> list = Arrays.asList("ccc", "ddd", "aaa", "eee", "bbb");
        list.stream().sorted().forEach(System.out::println);

        //定制排序(降序)
        Employee employee = employees.stream().sorted((e1, e2) -> e2.getAge() - e1.getAge()).findFirst().get();
        System.out.println(employee);
    }

    /**
     * 终止操作：查找与匹配
     * allMatch     检查是否匹配所有元素
     * anyMatch     检查是否至少匹配一个元素
     * noneMatch    检查是否没有匹配所有元素
     * findFirst    返回第一个元素
     * findAny      返回当前流中的任意元素
     * count        返回流中元素的总个数
     * max          返回流中的最大值
     * min          返回流中的最小值
     * forEach      内部迭代
     */
    @Test
    public void test5() {
        boolean b = employees.stream().allMatch(e -> e.getStatus().equals(Status.FREE.toString()));
        System.out.println(b);

        b = employees.stream().anyMatch(e -> e.getStatus().equals(Status.FREE.toString()));
        System.out.println(b);

        b = employees.stream().noneMatch(e -> e.getStatus().equals(Status.FREE.toString()));
        System.out.println(b);

        Optional<Employee> optional = employees.stream().sorted((e1, e2) -> ((Double) e1.getSalary()).compareTo(e2.getSalary())).findFirst();
        Employee employee = optional.orElse(null);
        System.out.println(employee);

        optional = employees.parallelStream().filter(e -> e.getStatus().equals(Status.FREE.toString())).findFirst();
        System.out.println(optional.get());

        long count = employees.stream().count();
        System.out.println(count);

        optional = employees.stream().max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
        System.out.println(optional.get());

        optional = employees.stream().min((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
        System.out.println(optional.get());
    }

    /**
     * 规约
     * reduce(T identity, BinaryOperator b) 可以将流中元素反复结合起来，得到一个值，返回T
     * reduce(BinaryOperator b) 可以将流中元素反复结合起来，得到一个值，返回Optional<T>
     */
    @Test
    public void Test6() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer sum = list.stream().reduce(2, Integer::sum);//等价于list.stream().reduce(2, (x, y) -> x +y);
        System.out.println(sum);
        Optional<Integer> op = list.stream().reduce(Integer::sum);//没初始值，返回Optional<Integer>
        System.out.println(op.get());

        Optional<Double> optional = employees.stream().map(Employee::getSalary).reduce(Double::sum);
        System.out.println(optional.get());
    }

    /**
     * 收集
     * collect  将流转换为其它形式。接收一个Collector接口的实现，用于给Stream中元素做汇总的方法
     */
    @Test
    public void test7() {
        List<String> list = employees.stream().map(Employee::getName).collect(Collectors.toList());
        System.out.println(list);
        Set<String> set = employees.stream().map(Employee::getName).collect(Collectors.toSet());
        System.out.println(set);

        LinkedHashSet<String> linkedHashSet = employees.stream().map(Employee::getName).collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println(linkedHashSet);

        //平均值
        Double aDouble = employees.stream().collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(aDouble);

        //总和
        aDouble = employees.stream().collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(aDouble);

        //最大值
        Optional<Employee> optional = employees.stream()
                .collect(Collectors.maxBy((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())));
        System.out.println(optional.get().getSalary());
        optional = employees.stream().max(Comparator.comparing(Employee::getSalary));
        System.out.println("最大值：" + optional.get().getSalary());

        //最小值
        Optional<Double> op = employees.stream().map(Employee::getSalary)
                .collect(Collectors.minBy((x, y) -> Double.compare(x, y)));
        System.out.println(op.get());
        op = employees.stream().map(Employee::getSalary).min(Comparator.comparingDouble(Double::doubleValue));
        System.out.println("最小值：" + op.get());

        //分组
        Map<String, List<Employee>> map = employees.stream().collect(Collectors.groupingBy(Employee::getStatus));
        System.out.println(map);

        //多级分组
        Map<String, Map<String, List<Employee>>> map1 = employees.stream().collect(
                Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
            if (e.getAge() <= 30) {
                return "青年";
            } else if (e.getAge() <= 50) {
                return "中年";
            } else {
                return "老年";
            }
        })));
        System.out.println(map1);

        //分区
        Map<Boolean, List<Employee>> map2 = employees.stream().collect(Collectors.partitioningBy(e -> e.getSalary() > 7000));
        System.out.println(map2);

        //统计
        DoubleSummaryStatistics dss = employees.stream().collect(Collectors.summarizingDouble(e -> e.getSalary()));
        System.out.println("总数：" + dss.getCount() + "，平均：" + dss.getAverage() + "，总和：" + dss.getSum()
                + "，最大值：" + dss.getMax() + "，最小值：" + dss.getMin());

        //连接
        String s = employees.stream().map(Employee::getName).collect(Collectors.joining(",", "=====", "====="));
        System.out.println(s);
    }

    @Data
    private class Employee {
        private String name;
        private int age;
        private double salary;
        private String status;

        public Employee(String name, int age, double salary, String status) {
            this.name = name;
            this.age = age;
            this.salary = salary;
            this.status = status;
        }
    }

    private enum Status {
        FREE,
        BUSY,
        VOCATION
    }
}
