package com.masm.mapper;

import com.masm.bean.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by masiming on 2017/11/18 22:37.
 */
@Mapper
public interface OrderDetailMapper {

    List<String> getDistinctUserIds();

    BigDecimal getOrderAmount(String userId);

    int insert(OrderDetail orderDetail);

    int deleteByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderDetail orderDetail);

    OrderDetail selectByPrimaryKey(String id);
}
