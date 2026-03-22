package com.grain.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grain.management.entity.CropType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CropTypeMapper extends BaseMapper<CropType> {
    @Select("SELECT * FROM crop_types WHERE is_active = true")
    List<CropType> findByIsActiveTrue();

    @Select("SELECT * FROM crop_types WHERE crop_name = #{cropName}")
    CropType findByCropName(@Param("cropName") String cropName);

    @Select("SELECT * FROM crop_types")
    Page<CropType> findAll(Page<CropType> pageable);
}