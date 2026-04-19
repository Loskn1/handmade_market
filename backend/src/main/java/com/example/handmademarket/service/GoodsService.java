package com.example.handmademarket.service;

import com.example.handmademarket.dto.CreateGoodsRequest;
import com.example.handmademarket.util.ResponseResult;

public interface GoodsService {

    ResponseResult listGoods();

    ResponseResult getGoods(Long id);

    ResponseResult createGoods(CreateGoodsRequest request, Long creatorId);

    ResponseResult updateGoods(Long id);

    ResponseResult offlineGoods(Long id, Long creatorId);

    ResponseResult getGoodsByCategory(String category);

    ResponseResult getGoodsByStatus(Integer status);

    ResponseResult searchGoods(String keyword);

    ResponseResult auditGoods(Long id, Integer status, String reason);
}
