package com.grain.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.grain.management.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    @Select("SELECT * FROM order_details WHERE order_id = #{orderId}")
    List<OrderDetail> findByOrderId(@Param("orderId") Integer orderId);

    @Select("DELETE FROM order_details WHERE order_id = #{orderId}")
    void deleteByOrderId(@Param("orderId") Integer orderId);
}