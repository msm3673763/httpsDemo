package com.masm.service;

import com.masm.bean.TOrderDetail;

import java.math.BigDecimal;

/**
 * Created by masiming on 2017/11/18 22:17.
 */
public interface OrderDetailService {

    int addOrderDetail(TOrderDetail orderDetail);

    int delOrderDetail(String id);

    int updateOrderDetail(TOrderDetail orderDetail);

    TOrderDetail getOrderDetail(String id);

    BigDecimal getOrderAmount(String userId);

}
