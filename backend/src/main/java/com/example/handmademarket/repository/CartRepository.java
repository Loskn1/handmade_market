package com.example.handmademarket.repository;

import com.example.handmademarket.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findByUserIdOrderByCreateTimeDesc(Integer userId);

    Optional<Cart> findByUserIdAndGoodsIdAndSpec(Integer userId, Integer goodsId, String spec);

    void deleteByCartIdAndUserId(Integer cartId, Integer userId);

    void deleteByUserId(Integer userId);

    long countByUserId(Integer userId);
}
