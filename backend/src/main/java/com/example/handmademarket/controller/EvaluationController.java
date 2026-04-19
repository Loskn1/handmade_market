package com.example.handmademarket.controller;

import com.example.handmademarket.dto.CreateEvaluationRequest;
import com.example.handmademarket.service.EvaluationService;
import com.example.handmademarket.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<ResponseResult> submitEvaluation(
            @RequestBody CreateEvaluationRequest request,
            @RequestHeader("X-User-Id") Long evaluatorId) {
        return ResponseEntity.ok(evaluationService.submitEvaluation(request, evaluatorId));
    }
}
