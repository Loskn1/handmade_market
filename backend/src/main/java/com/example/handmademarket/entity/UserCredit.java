package com.example.handmademarket.entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserCredit {
    // 主键
    private Long id;
    // 所属用户ID
    private Long userId;
    // 当前信用分数（默认初始100分）
    private Integer creditScore;
    // 信用等级：优秀/良好/一般/黑名单
    private String creditLevel;
    // 状态：1正常 0封禁
    private Integer status;
    // 数据最后更新时间
    private Date updateTime;
}
