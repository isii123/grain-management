package com.grain.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grain.management.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Optional<Order> findByOrderNo(@Param("orderNo") String orderNo);

    @Select("SELECT * FROM orders WHERE customer_id = #{customerId}")
    List<Order> findByCustomerId(@Param("customerId") Integer customerId);

    @Select("SELECT * FROM orders")
    Page<Order> findAll(Page<Order> pageable);

    @Select("SELECT * FROM orders WHERE order_date BETWEEN #{startDate} AND #{endDate}")
    List<Order> findByOrderDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Select("<script>" +
            "SELECT * FROM orders o " +
            "LEFT JOIN customers c ON o.customer_id = c.id " +
            "WHERE 1=1 " +
            "<if test='startDate != null'> AND o.order_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND o.order_date &lt;= #{endDate} </if>" +
            "<if test='customerName != null'> AND LOWER(c.customer_name) LIKE LOWER('%${customerName}%') </if>" +
            "</script>")
    Page<Order> searchOrders(@Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate,
                             @Param("customerName") String customerName,
                             Page<Order> pageable);

    @Select("<script>" +
            "SELECT SUM(o.total_amount), SUM(o.total_net_weight), COUNT(o.id) FROM orders o " +
            "WHERE 1=1 " +
            "<if test='startDate != null'> AND o.order_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND o.order_date &lt;= #{endDate} </if>" +
            "</script>")
    Object[] getOrderStatistics(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Select("SELECT * FROM orders WHERE order_date = #{date}")
    List<Order> findByOrderDate(@Param("date") LocalDate date);

    @Select("SELECT * FROM orders WHERE YEAR(order_date) = #{year} AND MONTH(order_date) = #{month}")
    List<Order> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}