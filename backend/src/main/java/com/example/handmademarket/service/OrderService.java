package com.example.handmademarket.service;

import com.example.handmademarket.util.ResponseResult;

public interface OrderService {

    ResponseResult listOrders();

    ResponseResult createOrder();

    ResponseResult getOrder(Long id);
}
