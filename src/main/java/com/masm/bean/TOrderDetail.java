package com.masm.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TOrderDetail {
    private String code;

    private String gtype;

    private String gcode;

    private Date createTime;

    private BigDecimal price;

    private String userId;

}