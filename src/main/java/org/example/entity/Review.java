package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评价实体
 */
@Data
@TableName("review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String targetType;
    private Long targetId;
    private Long merchantId;
    private Long userId;
    private Integer score;
    private String content;
    private String images;
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;

    /** 用户昵称（查询时填充） */
    @TableField(exist = false)
    private String userNickname;
    @TableField(exist = false)
    private String userAvatar;
}
