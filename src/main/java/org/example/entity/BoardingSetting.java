package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 寄养服务设置实体
 */
@Data
@TableName("boarding_setting")
public class BoardingSetting {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private BigDecimal smallDogPrice;
    private BigDecimal mediumDogPrice;
    private BigDecimal bigDogPrice;
    private BigDecimal catPrice;
    private Integer maxCapacityPerDay;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
