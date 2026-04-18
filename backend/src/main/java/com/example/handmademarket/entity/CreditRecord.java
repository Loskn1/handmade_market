package com.example.handmademarket.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CreditRecord {
    // 主键ID
    private Long id;
    // 被操作用户ID
    private Long userId;
    // 操作的管理员ID
    private Long adminId;
    // 分数变动值（加分正数、扣分负数）
    private Integer scoreChange;
    // 调整原因备注
    private String reason;
    // 操作时间
    private Date createTime;
}
