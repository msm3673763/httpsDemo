package com.masm.bean;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/25

 * Contributors:
 *      - initial implementation
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 暂无描述
 *
 * @author ucs_masiming
 * @since 2017/8/25
 */
public class Person {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("application.xml");
        Person person = (Person) ac.getBean("person");
        System.out.println(person.getAge());
    }
}
