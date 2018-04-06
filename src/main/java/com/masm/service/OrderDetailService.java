package com.masm.service;

import com.masm.bean.OrderDetail;

import java.math.BigDecimal;

/**
 * Created by masiming on 2017/11/18 22:17.
 */
public interface OrderDetailService {

    int addOrderDetail(OrderDetail orderDetail);

    int delOrderDetail(String id);

    int updateOrderDetail(OrderDetail orderDetail);

    OrderDetail getOrderDetail(String id);

    BigDecimal getOrderAmount(String userId);

    BigDecimal getOrderAmountByTemplate(String userId);

}
