package com.grain.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("price_history")
public class PriceHistory {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("crop_type_id")
    private Integer cropTypeId;

    @TableField("price_per_kg")
    private BigDecimal pricePerKg;

    @TableField("effective_date")
    private LocalDate effectiveDate;

    @TableField("expiry_date")
    private LocalDate expiryDate;

    @TableField("is_current")
    private Boolean isCurrent = true;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}