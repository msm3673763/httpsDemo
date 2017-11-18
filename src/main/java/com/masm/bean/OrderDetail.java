package com.masm.bean;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/25

 * Contributors:
 *      - initial implementation
 */

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ucs_masiming
 * @since 2017/8/25
 */
@Data
public class OrderDetail {

    private String code;
    private String gtype;
    private String gcode;
    private Date createTime;
    private BigDecimal price;
    private String userId;

}
