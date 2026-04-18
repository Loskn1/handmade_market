package com.example.handmademarket.controller;

import com.example.handmademarket.entity.Goods;
import com.example.handmademarket.repository.GoodsRepository;
import com.example.handmademarket.service.GoodsService;
import com.example.handmademarket.util.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GoodsController {

    private final GoodsService goodsService;
    private final GoodsRepository goodsRepository;

    public GoodsController(GoodsService goodsService, GoodsRepository goodsRepository) {
        this.goodsService = goodsService;
        this.goodsRepository = goodsRepository;
    }

    // 智能搜索接口
    @GetMapping("/search")
    public Result<List<Goods>> search(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "default") String sort
    ){
        return goodsService.searchGoods(keyword, sort);
    }

    // 智能推荐接口
    @GetMapping("/goods/recommend")
    public Result<List<Goods>> recommend(){
        return goodsService.recommendGoods();
    }

    // 【新增】商品详情接口：根据ID查询单个商品
    @GetMapping("/goods/{id}")
    public Result<Goods> getGoodsDetail(@PathVariable Long id) {
        Goods goods = goodsRepository.findById(id).orElse(null);
        return Result.success(goods);
    }
}