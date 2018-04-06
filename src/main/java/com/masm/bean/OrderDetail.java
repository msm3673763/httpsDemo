package com.masm.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细bean
 *
 * @author masiming
 * @create 2017/12/09
 **/
@Data
public class OrderDetail {
    private String code;

    private String gtype;

    private String gcode;

    private Date createTime;

    private BigDecimal price;

    private String userId;

}