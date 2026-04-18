package com.example.handmademarket.repository;

import com.example.handmademarket.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, String> {

    boolean existsByOrderId(String orderId);

    List<Evaluation> findByOrderId(String orderId);

    List<Evaluation> findByEvaluatedIdOrderByCreateTimeDesc(Integer evaluatedId);
=======
public interface EvaluationRepository extends JpaRepository<Evaluation, String> {

    boolean existsByOrderId(String orderId);
>>>>>>> my111
}
