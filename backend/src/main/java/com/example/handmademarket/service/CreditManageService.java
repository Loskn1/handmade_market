package com.example.handmademarket.service;

import com.example.handmademarket.entity.CreditRecord;
import com.example.handmademarket.entity.UserCredit;
import com.example.handmademarket.repository.CreditRecordRepository;
import com.example.handmademarket.repository.UserCreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 信用管理核心业务层
 * 对应：手作趣集-信用评分模块 + 管理员后台模块
 * 规则：初始80分，满分100 | 好评+1，差评-2，违规-5
 * 等级：高信用≥90 | 普通60-89 | 低信用≤60
 */
@Service
public class CreditManageService {

    @Autowired
    private UserCreditRepository userCreditRepository;

    @Autowired
    private CreditRecordRepository creditRecordRepository;

    // ====================== 基础查询 ======================
    /**
     * 管理员查询所有用户信用列表
     */
    public List<UserCredit> getAllUserCredit() {
        return userCreditRepository.findAll();
    }

    /**
     * 根据用户ID查询单个信用信息
     */
    public UserCredit getUserCreditByUserId(Long userId) {
        return userCreditRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户信用数据不存在"));
    }

    // ====================== 核心信用调整 ======================
    /**
     * 管理员手动调整用户信用分（自动等级判定+日志记录）
     * @param userId        被调整用户ID
     * @param adminId       操作管理员ID
     * @param scoreChange   分数变动（+1好评/-2差评/-5违规）
     * @param reason        调整原因
     */
    @Transactional(rollbackFor = Exception.class)
    public void adjustUserCredit(Long userId, Long adminId, Integer scoreChange, String reason) {
        // 1. 获取用户信用数据
        UserCredit credit = userCreditRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户信用档案不存在"));

        // 2. 计算最新分数（遵守文档：满分100，初始80）
        int finalScore = credit.getCreditScore() + scoreChange;
        if (finalScore > 100) {
            finalScore = 100; // 封顶100分
        }
        credit.setCreditScore(finalScore);

        // 3. 文档官方信用等级判定
        if (finalScore >= 90) {
            credit.setCreditLevel("高信用");
        } else if (finalScore >= 60) {
            credit.setCreditLevel("普通信用");
        } else {
            credit.setCreditLevel("低信用");
            credit.setStatus(0); // 低信用限制权限
        }

        credit.setUpdateTime(new Date());
        userCreditRepository.save(credit);

        // 4. 记录管理员操作日志（全程留痕）
        CreditRecord record = new CreditRecord();
        record.setUserId(userId);
        record.setAdminId(adminId);
        record.setScoreChange(scoreChange);
        record.setReason(reason);
        record.setCreateTime(new Date());
        creditRecordRepository.save(record);
    }

    // ====================== 信用申诉模块（文档必选） ======================
    /**
     * 普通管理员 - 申诉初审
     */
    public void appealFirstCheck(Long recordId, Integer checkStatus) {
        Optional<CreditRecord> recordOptional = creditRecordRepository.findById(recordId);
        if (recordOptional.isEmpty()) {
            throw new RuntimeException("申诉记录不存在");
        }
        // 初审逻辑：1=通过 2=驳回
        CreditRecord record = recordOptional.get();
        record.setReason(record.getReason() + "【初审：" + (checkStatus == 1 ? "通过" : "驳回") + "】");
        creditRecordRepository.save(record);
    }

    /**
     * 超级管理员 - 申诉终审（可修正分数）
     */
    @Transactional
    public void appealFinalCheck(Long userId, Long recordId, Integer finalStatus, Integer fixScore) {
        // 仅超级管理员可调用
        if (finalStatus != 1) {
            throw new RuntimeException("申诉终审驳回");
        }

        // 终审通过 → 修正信用分
        UserCredit credit = getUserCreditByUserId(userId);
        credit.setCreditScore(credit.getCreditScore() + fixScore);
        credit.setUpdateTime(new Date());
        userCreditRepository.save(credit);

        // 更新日志
        CreditRecord record = creditRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("记录不存在"));
        record.setReason(record.getReason() + "【终审通过，修正分数：" + fixScore + "】");
        creditRecordRepository.save(record);
    }
}
