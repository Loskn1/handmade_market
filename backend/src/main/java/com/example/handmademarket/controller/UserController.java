package com.example.handmademarket.controller;

import com.example.handmademarket.util.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<ResponseResult> getProfile() {
        // TODO: implement user profile retrieval
        return ResponseEntity.ok(ResponseResult.ok("用户信息接口骨架"));
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseResult> updateProfile() {
        // TODO: implement user profile update
        return ResponseEntity.ok(ResponseResult.ok("更新用户信息接口骨架"));
    }
}
