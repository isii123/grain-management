package com.grain.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_details")
public class OrderDetail {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("order_id")
    private Integer orderId;

    @TableField("vehicle_no")
    private String vehicleNo;

    @TableField("gross_weight")
    private BigDecimal grossWeight;

    @TableField("tare_weight")
    private BigDecimal tareWeight;

    @TableField("net_weight")
    private BigDecimal netWeight;

    @TableField("crop_type_id")
    private Integer cropTypeId;

    @TableField("price_per_kg")
    private BigDecimal pricePerKg;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("remark")
    private String remark;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}