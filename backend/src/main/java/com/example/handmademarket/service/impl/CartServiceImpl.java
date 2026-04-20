package com.example.handmademarket.service.impl;

import com.example.handmademarket.dto.CartCheckoutRequest;
import com.example.handmademarket.dto.CartRequest;
import com.example.handmademarket.dto.CreateOrderRequest;
import com.example.handmademarket.entity.Cart;
import com.example.handmademarket.entity.Goods;
import com.example.handmademarket.entity.User;
import com.example.handmademarket.repository.CartRepository;
import com.example.handmademarket.repository.GoodsRepository;
import com.example.handmademarket.repository.UserRepository;
import com.example.handmademarket.service.CartService;
import com.example.handmademarket.service.OrderService;
import com.example.handmademarket.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    private Integer getUserId(String username) {
        Optional<User> userOpt = userRepository.findByUserAccount(username);
        return userOpt.map(User::getUserId).orElse(null);
    }

    @Override
    public ResponseResult getCartList(String username) {
        Integer userId = getUserId(username);
        if (userId == null) return ResponseResult.fail("用户不存在");

        List<Cart> cartList = cartRepository.findByUserIdOrderByCreateTimeDesc(userId);

        // 组装购物车详情（包含商品信息）
        List<Map<String, Object>> result = new ArrayList<>();
        for (Cart cart : cartList) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("cartId", cart.getCartId());
            item.put("goodsId", cart.getGoodsId());
            item.put("goodsImage", cart.getGoodsImage());
            item.put("quantity", cart.getQuantity());
            item.put("spec", cart.getSpec());
            item.put("createTime", cart.getCreateTime());

            // 查询商品信息
            Optional<Goods> goodsOpt = goodsRepository.findById(Long.valueOf(cart.getGoodsId()));
            if (goodsOpt.isPresent()) {
                Goods goods = goodsOpt.get();
                item.put("name", goods.getGoodsName());
                item.put("price", goods.getPrice());
                item.put("imageUrl", goods.getImageUrl());
                item.put("stock", goods.getStock());
                item.put("status", goods.getStatus());
            }
            result.add(item);
        }

        return ResponseResult.ok("获取购物车成功", result);
    }

    @Override
    @Transactional
    public ResponseResult addToCart(String username, CartRequest request) {
        Integer userId = getUserId(username);
        if (userId == null) return ResponseResult.fail("用户不存在");

        if (request.getGoodsId() == null) return ResponseResult.fail("商品ID不能为空");
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            return ResponseResult.fail("数量必须大于0");
        }

        // 检查商品是否存在且已上架
        Optional<Goods> goodsOpt = goodsRepository.findById(Long.valueOf(request.getGoodsId()));
        if (goodsOpt.isEmpty()) return ResponseResult.fail("商品不存在");

        Goods goods = goodsOpt.get();
        if (goods.getStatus() == null || goods.getStatus() != 1) {
            return ResponseResult.fail("商品未上架");
        }

        String spec = (request.getSpec() == null || request.getSpec().isEmpty()) ? "标准款" : request.getSpec();

        // 检查是否已存在相同商品+规格
        Optional<Cart> existing = cartRepository.findByUserIdAndGoodsIdAndSpec(userId, request.getGoodsId(), spec);
        if (existing.isPresent()) {
            Cart cart = existing.get();
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
            cartRepository.save(cart);
            return ResponseResult.ok("商品已在购物车中，数量已更新");
        }

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setGoodsId(request.getGoodsId());
        cart.setGoodsImage(goods.getImageUrl());
        cart.setQuantity(request.getQuantity());
        cart.setSpec(spec);
        cartRepository.save(cart);

        return ResponseResult.ok("添加购物车成功");
    }

    @Override
    @Transactional
    public ResponseResult updateQuantity(String username, Integer cartId, Integer quantity) {
        Integer userId = getUserId(username);
        if (userId == null) return ResponseResult.fail("用户不存在");

        if (quantity == null || quantity <= 0) return ResponseResult.fail("数量必须大于0");

        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty()) return ResponseResult.fail("购物车记录不存在");

        Cart cart = cartOpt.get();
        if (!cart.getUserId().equals(userId)) return ResponseResult.fail("无权操作");

        cart.setQuantity(quantity);
        cartRepository.save(cart);

        return ResponseResult.ok("更新数量成功");
    }

    @Override
    @Transactional
    public ResponseResult removeFromCart(String username, Integer cartId) {
        Integer userId = getUserId(username);
        if (userId == null) return ResponseResult.fail("用户不存在");

        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty()) return ResponseResult.fail("购物车记录不存在");

        Cart cart = cartOpt.get();
        if (!cart.getUserId().equals(userId)) return ResponseResult.fail("无权操作");

        cartRepository.delete(cart);
        return ResponseResult.ok("删除成功");
    }

    @Override
    @Transactional
    public ResponseResult clearCart(String username) {
        Integer userId = getUserId(username);
        if (userId == null) return ResponseResult.fail("用户不存在");

        cartRepository.deleteByUserId(userId);
        return ResponseResult.ok("购物车已清空");
    }

    @Override
    @Transactional
    public ResponseResult checkout(String username, CartCheckoutRequest request) {
        Integer userId = getUserId(username);
        if (userId == null) return ResponseResult.fail("用户不存在");

        if (request.getCartIds() == null || request.getCartIds().isEmpty()) {
            return ResponseResult.fail("请选择要结算的商品");
        }
        if (request.getDeliveryAddress() == null || request.getDeliveryAddress().isBlank()) {
            return ResponseResult.fail("收货地址不能为空");
        }

        // 查询选中的购物车项
        List<Cart> cartItems = new ArrayList<>();
        for (Integer cartId : request.getCartIds()) {
            Optional<Cart> cartOpt = cartRepository.findById(cartId);
            if (cartOpt.isEmpty()) return ResponseResult.fail("购物车记录不存在: " + cartId);
            Cart cart = cartOpt.get();
            if (!cart.getUserId().equals(userId)) return ResponseResult.fail("无权操作");
            cartItems.add(cart);
        }

        // 将购物车项转换为订单请求
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        List<CreateOrderRequest.OrderItemDTO> items = new ArrayList<>();
        for (Cart cart : cartItems) {
            CreateOrderRequest.OrderItemDTO item = new CreateOrderRequest.OrderItemDTO();
            item.setGoodsId(cart.getGoodsId());
            item.setQuantity(cart.getQuantity());
            items.add(item);
        }
        orderRequest.setItems(items);
        orderRequest.setDeliveryAddress(request.getDeliveryAddress());
        orderRequest.setPayType(request.getPayType());
        orderRequest.setRemark(request.getRemark());

        // 调用订单服务创建订单
        ResponseResult result = orderService.createOrder(username, orderRequest);

        // 订单创建成功后，删除已结算的购物车项
        if (result.getCode() == 200) {
            for (Cart cart : cartItems) {
                cartRepository.delete(cart);
            }
        }

        return result;
    }
}
