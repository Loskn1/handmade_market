package com.example.handmademarket.service;

import com.example.handmademarket.dto.CreateEvaluationRequest;
import com.example.handmademarket.util.ResponseResult;

public interface EvaluationService {

    ResponseResult submitEvaluation(CreateEvaluationRequest request, Long evaluatorId);
}
