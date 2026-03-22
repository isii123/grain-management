package com.grain.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("order_no")
    private String orderNo;

    @TableField("customer_id")
    private Integer customerId;

    @TableField("order_date")
    private LocalDate orderDate;

    @TableField("total_vehicles")
    private Integer totalVehicles = 0;

    @TableField("total_net_weight")
    private BigDecimal totalNetWeight = BigDecimal.ZERO;

    @TableField("total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @TableField("avg_price")
    private BigDecimal avgPrice = BigDecimal.ZERO;

    @TableField("remark")
    private String remark;

    @TableField("status")
    private String status = "active";

    @TableField("created_by")
    private Integer createdBy;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}