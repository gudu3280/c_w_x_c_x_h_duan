package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 美容服务项实体
 */
@Data
@TableName("grooming_service")
public class GroomingService {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private String serviceName;
    private Integer duration;
    private BigDecimal price;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
