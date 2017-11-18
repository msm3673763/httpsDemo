package com.masm.cache.service.impl;

import com.masm.cache.service.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/11/11.
 */
public class AtomicOrderServiceImpl implements OrderService {

    static AtomicInteger at = new AtomicInteger();

    @Override
    public String getOrderId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmSS");
        return formatter.format(LocalDateTime.now()) + at.incrementAndGet();
    }
}
