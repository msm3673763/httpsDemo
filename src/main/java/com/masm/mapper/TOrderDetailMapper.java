package com.masm.mapper;

import com.masm.bean.TOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TOrderDetailMapper {
    int deleteByPrimaryKey(String code);

    int insert(TOrderDetail record);

    int insertSelective(TOrderDetail record);

    TOrderDetail selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(TOrderDetail record);

    int updateByPrimaryKey(TOrderDetail record);

    List<String> getDistinctUserIds();

    BigDecimal getOrderAmount(@Param("userId") String userId);
}