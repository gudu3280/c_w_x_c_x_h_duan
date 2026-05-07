package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家实体
 */
@Data
@TableName("merchant")
public class Merchant {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String type;
    private String address;
    private BigDecimal lng;
    private BigDecimal lat;
    private String phone;
    private String businessHours;
    private String description;
    private String coverImage;
    private String images;
    private String tags;
    private BigDecimal avgScore;
    private Integer reviewCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    /** 距离（查询时计算，非数据库字段） */
    @TableField(exist = false)
    private Double distance;
}
