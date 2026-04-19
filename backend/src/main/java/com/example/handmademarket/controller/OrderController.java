package com.example.handmademarket.controller;

import com.example.handmademarket.util.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping
    public ResponseEntity<ResponseResult> listOrders() {
        // TODO: implement order list for user and admin
        return ResponseEntity.ok(ResponseResult.ok("订单列表接口骨架"));
    }

    @PostMapping
    public ResponseEntity<ResponseResult> createOrder() {
        // TODO: implement create order logic
        return ResponseEntity.ok(ResponseResult.ok("创建订单接口骨架"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult> getOrder(@PathVariable Long id) {
        // TODO: implement order detail
        return ResponseEntity.ok(ResponseResult.ok("订单详情接口骨架"));
    }
}
