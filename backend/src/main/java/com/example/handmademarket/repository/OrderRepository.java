package com.example.handmademarket.repository;

import com.example.handmademarket.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    long countByGoodsIdAndStatus(Long goodsId, String status);
}

