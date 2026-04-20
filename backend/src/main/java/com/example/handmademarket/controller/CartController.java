package com.example.handmademarket.controller;

import com.example.handmademarket.dto.CartCheckoutRequest;
import com.example.handmademarket.dto.CartRequest;
import com.example.handmademarket.service.CartService;
import com.example.handmademarket.util.JwtUtil;
import com.example.handmademarket.util.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    public CartController(CartService cartService, JwtUtil jwtUtil) {
        this.cartService = cartService;
        this.jwtUtil = jwtUtil;
    }

    private String extractUsername(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return "testuser";
        }
        if (!authHeader.startsWith("Bearer ")) {
            return "testuser";
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return "testuser";
        }
        return jwtUtil.getUsernameFromToken(token);
    }

    /** 获取购物车列表 */
    @GetMapping
    public ResponseEntity<ResponseResult> getCartList(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(cartService.getCartList(username));
    }

    /** 添加商品到购物车 */
    @PostMapping
    public ResponseEntity<ResponseResult> addToCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CartRequest request) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(cartService.addToCart(username, request));
    }

    /** 更新购物车商品数量 */
    @PutMapping("/{cartId}")
    public ResponseEntity<ResponseResult> updateQuantity(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Integer cartId,
            @RequestParam Integer quantity) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(cartService.updateQuantity(username, cartId, quantity));
    }

    /** 删除购物车中的商品 */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ResponseResult> removeFromCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Integer cartId) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(cartService.removeFromCart(username, cartId));
    }

    /** 清空购物车 */
    @DeleteMapping
    public ResponseEntity<ResponseResult> clearCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(cartService.clearCart(username));
    }

    /** 购物车结算：选中商品创建订单 */
    @PostMapping("/checkout")
    public ResponseEntity<ResponseResult> checkout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CartCheckoutRequest request) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(cartService.checkout(username, request));
    }
}
