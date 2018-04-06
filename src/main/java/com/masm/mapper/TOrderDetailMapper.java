package com.masm.mapper;

import com.masm.bean.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TOrderDetailMapper {
    int deleteByPrimaryKey(String code);

    int insert(OrderDetail record);

    int insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(OrderDetail record);

    int updateByPrimaryKey(OrderDetail record);

    List<String> getDistinctUserIds();

    BigDecimal getOrderAmount(@Param("userId") String userId);
}