package com.example.handmademarket.controller;

import com.example.handmademarket.util.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @GetMapping
    public ResponseEntity<ResponseResult> listGoods() {
        // TODO: implement goods list and search
        return ResponseEntity.ok(ResponseResult.ok("商品列表接口骨架"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult> getGoods(@PathVariable Long id) {
        // TODO: implement goods detail fetch
        return ResponseEntity.ok(ResponseResult.ok("商品详情接口骨架"));
    }

    @PostMapping
    public ResponseEntity<ResponseResult> createGoods() {
        // TODO: implement publish goods logic
        return ResponseEntity.ok(ResponseResult.ok("发布商品接口骨架"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseResult> updateGoods(@PathVariable Long id) {
        // TODO: implement update goods logic
        return ResponseEntity.ok(ResponseResult.ok("修改商品接口骨架"));
    }
}
