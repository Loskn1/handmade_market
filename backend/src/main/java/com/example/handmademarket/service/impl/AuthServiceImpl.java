package com.example.handmademarket.service.impl;

import com.example.handmademarket.dto.LoginRequest;
import com.example.handmademarket.dto.RegisterRequest;
import com.example.handmademarket.repository.UserRepository;
import com.example.handmademarket.service.AuthService;
import com.example.handmademarket.util.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseResult login(LoginRequest request) {
        // TODO: implement authentication logic and JWT token generation
        return ResponseResult.ok("登录接口骨架");
    }

    @Override
    public ResponseResult register(RegisterRequest request) {
        // TODO: implement registration logic, validation and role assignment
        return ResponseResult.ok("注册接口骨架");
    }
}
