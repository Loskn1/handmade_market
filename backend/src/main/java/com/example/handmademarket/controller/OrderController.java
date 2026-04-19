package com.example.handmademarket.controller;

import com.example.handmademarket.dto.CreateOrderRequest;
import com.example.handmademarket.dto.EvaluationRequest;
import com.example.handmademarket.service.OrderService;
import com.example.handmademarket.util.JwtUtil;
import com.example.handmademarket.util.ResponseResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    // 从 Authorization header 中提取用户名，如果没有则使用测试用户
    private String extractUsername(String authHeader) {
        // 测试模式：如果没有 token，使用默认测试用户
        if (authHeader == null || authHeader.isBlank()) {
            return "testuser";  // 默认测试用户
        }
        
        if (!authHeader.startsWith("Bearer ")) {
            return "testuser";  // 默认测试用户
        }
        
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return "testuser";  // token 无效时也用测试用户
        }
        return jwtUtil.getUsernameFromToken(token);
    }

    /** 买家：获取我的订单列表（支持状态筛选） */
    @GetMapping("/user/orders")
    public ResponseEntity<ResponseResult> getBuyerOrders(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer status) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(orderService.getBuyerOrders(username, status));
    }

    /** 卖家：获取销售订单列表（支持状态筛选） */
    @GetMapping("/seller/orders")
    public ResponseEntity<ResponseResult> getSellerOrders(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer status) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(orderService.getSellerOrders(username, status));
    }

    /** 卖家：获取销售统计 */
    @GetMapping("/seller/stats")
    public ResponseEntity<ResponseResult> getSellerStats(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String username = extractUsername(authHeader);
        return ResponseEntity.ok(orderService.getSellerStats(username));
    }

    /** 管理员：获取所有订单 */
    @GetMapping("/admin/orders")
    public ResponseEntity<ResponseResult> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /** 管理员：按条件搜索订单 */
    @GetMapping("/admin/orders/search")
    public ResponseEntity<ResponseResult> searchOrders(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer buyerId,
            @RequestParam(required = false) Integer sellerId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer orderType) {
        return ResponseEntity.ok(orderService.searchOrders(orderNo, buyerId, sellerId,
                status, startTime, endTime, orderType));
    }

    /** 管理员：处理订单纠纷 */
    @PutMapping("/admin/orders/{orderId}/dispute")
    public ResponseEntity<ResponseResult> handleDispute(
            @PathVariable String orderId,
            @RequestBody Map<String, String> body) {
        String disputeResult = body != null ? body.get("disputeResult") : null;
        return ResponseEntity.ok(orderService.handleDispute(orderId, disputeResult));
    }

    /** 管理员：导出订单Excel */
    @GetMapping("/admin/orders/export")
    public void exportOrders(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer orderType,
            HttpServletResponse response) {
        orderService.exportOrders(status, startTime, endTime, orderType, response);
    }

    /** 管理员：订单统计概览 */
    @GetMapping("/admin/orders/stats")
    public ResponseEntity<ResponseResult> getAdminOrderStats() {
        return ResponseEntity.ok(orderService.getAdminOrderStats());
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
