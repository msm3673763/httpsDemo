package com.masm.distribute.service.impl;

import com.masm.distribute.service.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Administrator on 2017/11/11.
 */
public class NoLockOrderServiceImpl implements OrderService {

    int num;

    @Override
    public String getOrderId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmSS");
        return formatter.format(LocalDateTime.now()) + num++;
    }
}
