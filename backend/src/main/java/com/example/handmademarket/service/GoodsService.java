package com.example.handmademarket.service;

import com.example.handmademarket.util.ResponseResult;

public interface GoodsService {

    ResponseResult listGoods();

    ResponseResult getGoods(Long id);

    ResponseResult createGoods();

    ResponseResult updateGoods(Long id);
}
