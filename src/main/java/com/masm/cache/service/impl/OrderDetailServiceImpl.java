package com.masm.cache.service.impl;

import com.masm.cache.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Override
    public BigDecimal getOrderAmount(String userCode) {
        //从缓存中取，获取不存在，查询数据库并将结果放入缓存
        return BigDecimal.valueOf(348927492343.00);
    }
}
