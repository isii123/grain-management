package com.grain.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grain.management.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    @Select("SELECT * FROM customers WHERE customer_name = #{customerName}")
    Customer findByCustomerName(@Param("customerName") String customerName);

    @Select("SELECT * FROM customers WHERE LOWER(customer_name) LIKE LOWER('%${customerName}%')")
    List<Customer> findByCustomerNameContainingIgnoreCase(@Param("customerName") String customerName);

    @Select("SELECT * FROM customers WHERE is_active = true")
    List<Customer> findByIsActiveTrue();

    @Select("SELECT COUNT(*) FROM customers WHERE customer_name = #{customerName}")
    boolean existsByCustomerName(@Param("customerName") String customerName);

    @Select("SELECT * FROM customers")
    Page<Customer> findAll(Page<Customer> pageable);
}