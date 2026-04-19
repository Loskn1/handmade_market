package com.example.handmademarket.service;

import com.example.handmademarket.entity.Goods;
import com.example.handmademarket.repository.GoodsRepository;
import com.example.handmademarket.util.Result;
import org.springframework.stereotype.Service;

import java.util.List;

// 【重点】public类名 GoodsService 和文件名 GoodsService.java 完全一致！
@Service
public class GoodsService {

    // 构造器注入（SpringBoot3官方写法，无@Autowired所有报错）
    private final GoodsRepository goodsRepository;

    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    // 智能搜索业务方法
    public Result<List<Goods>> searchGoods(String keyword, String sort) {
        List<Goods> goodsList;

        if (keyword == null || keyword.trim().isEmpty()) {
            goodsList = goodsRepository.findAll();
        } else {
            goodsList = goodsRepository.searchByKeyword(keyword);
        }

        // 多维度排序
        switch (sort) {
            case "priceAsc":
                goodsList.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
                break;
            case "priceDesc":
                goodsList.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
                break;
            case "salesDesc":
                goodsList.sort((a, b) -> b.getSales().compareTo(a.getSales()));
                break;
            default:
                break;
        }

        return Result.success(goodsList);
    }

    // 智能推荐业务方法
    public Result<List<Goods>> recommendGoods() {
        List<Goods> recommendList = goodsRepository.findTop10ByOrderBySalesDesc();
        return Result.success(recommendList);
    }
}