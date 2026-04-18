package com.example.handmademarket.repository;

import com.example.handmademarket.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    // 智能搜索：关键词模糊匹配 商品名、分类、描述
    @Query("SELECT g FROM Goods g WHERE " +
            "g.goodsName LIKE %:keyword% " +
            "OR g.category LIKE %:keyword% " +
            "OR g.description LIKE %:keyword%")
    List<Goods> searchByKeyword(@Param("keyword") String keyword);

    // 智能推荐：查询销量最高的前10个商品（JPA自动方法，无需写SQL）
    List<Goods> findTop10ByOrderBySalesDesc();
}