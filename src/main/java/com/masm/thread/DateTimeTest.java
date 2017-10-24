package com.masm.thread;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Created by masiming on 2017/10/20.
 */
public class DateTimeTest {

    @Test
    public void dateTest() {
        LocalDate today = LocalDate.now();
        System.out.println("当前日期：" + today);

        LocalDate firstDay_2014 = LocalDate.of(2014, Month.JANUARY, 1);
        System.out.println("2014年第一天：" + firstDay_2014);

        LocalDate todayAsia = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        System.out.println("亚洲时区的当前日期：" + todayAsia);

        LocalDate ofEpochDay = LocalDate.ofEpochDay(365);
        System.out.println("基准日期点" + ofEpochDay);

        LocalDate ofYearDay = LocalDate.ofYearDay(2014, 100);
        System.out.println("2014年第100天的日期" + ofYearDay);
    }

    @Test
    public void timeTest() {
        LocalTime now = LocalTime.now();
        System.out.println(now);

        LocalTime setTime = LocalTime.of(23, 25, 45, 20);
        System.out.println(setTime);

        LocalTime timeOfAsia = LocalTime.now(ZoneId.of("Asia/Kolkata"));
        System.out.println(timeOfAsia);

        LocalTime ofSecondOfDay = LocalTime.ofSecondOfDay(10000);
        System.out.println(ofSecondOfDay);
    }

    @Test
    public void localDateTimeTest() {
        LocalDateTime today = LocalDateTime.now();
        System.out.println("当前日期时间 DateTime=" + today);

        today = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        System.out.println("当前日期时间=" + today);

        today = LocalDateTime.of(2017, Month.JANUARY, 1,10, 10, 34);
        System.out.println(today);

        today = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        System.out.println("Current Date in IST=" + today);
    }

    @Test
    public void InstantTest() {
        Instant instant = Instant.now();
        System.out.println(instant);

        instant = Instant.ofEpochMilli(instant.toEpochMilli());
        System.out.println(instant);

        Duration duration = Duration.ofDays(30);
        System.out.println(duration);
    }

    @Test
    public void dateTimeFormatterTest() {
        LocalDate today = LocalDate.now();
        System.out.println("today:" + today);
        System.out.println(today.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        System.out.println(today.format(DateTimeFormatter.BASIC_ISO_DATE));

        LocalDateTime now = LocalDateTime.now();
        System.out.println("now:" + now);
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:SS")));
        System.out.println(now.format(DateTimeFormatter.BASIC_ISO_DATE));

        Instant timestamp = Instant.now();
        System.out.println("Default format of Instant="+timestamp);
    }


}
