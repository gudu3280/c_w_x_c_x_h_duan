package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约订单实体
 */
@Data
@TableName("order_booking")
public class OrderBooking {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private Long merchantId;
    private Long petId;
    private String orderType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long groomingServiceId;
    private String timeSlot;
    private BigDecimal totalAmount;
    private String status;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
