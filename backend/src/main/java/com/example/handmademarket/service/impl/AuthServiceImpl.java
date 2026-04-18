package com.example.handmademarket.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.handmademarket.dto.LoginRequest;
import com.example.handmademarket.dto.RegisterRequest;
import com.example.handmademarket.entity.User;
import com.example.handmademarket.repository.UserRepository;
import com.example.handmademarket.service.AuthService;
import com.example.handmademarket.util.JwtUtil;
import com.example.handmademarket.util.ResponseResult;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseResult login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseResult.fail("用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseResult.fail("密码不能为空");
        }

        Optional<User> userOptional = userRepository.findByUserAccount(request.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseResult.fail("用户名或密码错误");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseResult.fail("用户名或密码错误");
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUserAccount());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userAccount", user.getUserAccount());
        data.put("userName", user.getUserName());
        data.put("role", user.getRole());
        data.put("userId", user.getUser_id());

        return ResponseResult.ok("登录成功", data);
    }

    @Override
    public ResponseResult register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseResult.fail("用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseResult.fail("密码不能为空");
        }

        if (userRepository.existsByUserAccount(request.getUsername())) {
            return ResponseResult.fail("用户名已存在");
        }

        User user = new User();
        user.setUserAccount(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole() != null ? request.getRole() : "1");
        user.setRegisterTime(LocalDateTime.now());
        user.setStatus(1);
        user.setCreditScore(80);

        userRepository.save(user);

        return ResponseResult.ok("注册成功");
    }
}
