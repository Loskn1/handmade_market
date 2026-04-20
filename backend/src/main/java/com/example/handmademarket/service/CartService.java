package com.example.handmademarket.service;

import com.example.handmademarket.dto.CartCheckoutRequest;
import com.example.handmademarket.dto.CartRequest;
import com.example.handmademarket.util.ResponseResult;

public interface CartService {

    /** 获取用户购物车列表 */
    ResponseResult getCartList(String username);

    /** 添加商品到购物车 */
    ResponseResult addToCart(String username, CartRequest request);

    /** 更新购物车商品数量 */
    ResponseResult updateQuantity(String username, Integer cartId, Integer quantity);

    /** 删除购物车中的商品 */
    ResponseResult removeFromCart(String username, Integer cartId);

    /** 清空购物车 */
    ResponseResult clearCart(String username);

    /** 购物车结算：根据选中的购物车项创建订单 */
    ResponseResult checkout(String username, CartCheckoutRequest request);
}
