package com.example.handmademarket.service.impl;

import com.example.handmademarket.dto.CreateEvaluationRequest;
import com.example.handmademarket.entity.Evaluation;
import com.example.handmademarket.repository.EvaluationRepository;
import com.example.handmademarket.service.EvaluationService;
import com.example.handmademarket.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public ResponseResult submitEvaluation(CreateEvaluationRequest request, Long evaluatorId) {
        if (request.getOrderId() == null) {
            return ResponseResult.fail("订单ID不能为空");
        }

        if (request.getEvaluatedId() == null) {
            return ResponseResult.fail("被评价人ID不能为空");
        }

        if (request.getScore() == null || request.getScore() < 1 || request.getScore() > 5) {
            return ResponseResult.fail("评分必须在1-5之间");
        }

        if (request.getContent() == null || request.getContent().isEmpty()) {
            return ResponseResult.fail("评价内容不能为空");
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setEvalId(String.valueOf(System.currentTimeMillis()));
        evaluation.setOrderId(request.getOrderId());
        evaluation.setEvaluatorId(evaluatorId);
        evaluation.setEvaluatedId(request.getEvaluatedId());
        evaluation.setScore(request.getScore());
        evaluation.setContent(request.getContent());
        evaluation.setImages(request.getImages());
        evaluation.setStatus(0);
        evaluation.setCreateTime(LocalDateTime.now());

        Evaluation savedEvaluation = evaluationRepository.save(evaluation);
        return ResponseResult.ok(savedEvaluation);
    }
}
