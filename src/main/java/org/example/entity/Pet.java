package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 宠物档案实体
 */
@Data
@TableName("pet")
public class Pet {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String breed;
    private Integer gender;
    private LocalDate birthday;
    private BigDecimal weight;
    private Integer isNeutered;
    private String vaccineRecord;
    private String medicalHistory;
    private String habit;
    private String photoUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
