package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 可预约日历实体
 */
@Data
@TableName("service_calendar")
public class ServiceCalendar {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private LocalDate serviceDate;
    private Integer totalCapacity;
    private Integer usedCapacity;
    private Integer isClosed;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
