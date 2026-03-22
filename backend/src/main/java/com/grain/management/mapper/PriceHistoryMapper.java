package com.grain.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grain.management.entity.PriceHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PriceHistoryMapper extends BaseMapper<PriceHistory> {
    @Select("SELECT * FROM price_history WHERE crop_type_id = #{cropTypeId}")
    List<PriceHistory> findByCropTypeId(@Param("cropTypeId") Integer cropTypeId);

    @Select("SELECT * FROM price_history WHERE crop_type_id = #{cropTypeId} AND is_current = true LIMIT 1")
    PriceHistory findCurrentPriceByCropTypeId(@Param("cropTypeId") Integer cropTypeId);

    @Select("SELECT * FROM price_history WHERE crop_type_id = #{cropTypeId} AND effective_date <= #{date} AND (expiry_date >= #{date} OR expiry_date IS NULL) ORDER BY effective_date DESC LIMIT 1")
    PriceHistory findPriceByCropTypeAndDate(
            @Param("cropTypeId") Integer cropTypeId,
            @Param("date") LocalDate date);

    @Select("SELECT * FROM price_history WHERE is_current = true")
    List<PriceHistory> findByIsCurrentTrue();

    @Select("SELECT * FROM price_history")
    Page<PriceHistory> findAll(Page<PriceHistory> pageable);
}