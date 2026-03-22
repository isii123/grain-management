package com.grain.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("crop_types")
public class CropType {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("crop_name")
    private String cropName;

    @TableField("is_active")
    private Boolean isActive = true;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}