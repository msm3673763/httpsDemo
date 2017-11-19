package com.masm.cache.service;

import java.math.BigDecimal;

public interface OrderDetailService {

    BigDecimal getOrderAmount(String userCode);
}
