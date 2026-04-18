package com.example.handmademarket.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.handmademarket.dto.LoginRequest;
import com.example.handmademarket.dto.RegisterRequest;
import com.example.handmademarket.entity.Admin;
import com.example.handmademarket.entity.User;
import com.example.handmademarket.repository.AdminRepository;
import com.example.handmademarket.repository.UserRepository;
import com.example.handmademarket.service.AuthService;
import com.example.handmademarket.util.JwtUtil;
import com.example.handmademarket.util.ResponseResult;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final String DEFAULT_PASSWORD = "a123456";

    public AuthServiceImpl(UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 生成唯一账号
    private String generateUniqueUserAccount() {
        String userAccount;
        do {
            userAccount = "user" + System.currentTimeMillis();
        } while (userRepository.existsByUserAccount(userAccount));
        return userAccount;
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
            return ResponseResult.fail("账号或密码错误");
        }
        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            Integer errorCount = user.getPwdErrorCount();
            if (errorCount == null) {
                errorCount = 1;
            } else {
                errorCount++;
            }
            user.setPwdErrorCount(errorCount);

            if (errorCount >= 5) { // 5次错误锁定
                user.setStatus(2);
                user.setLockTime(new Date());
            }
            userRepository.save(user);
            return ResponseResult.fail("密码错误，剩余" + (5 - errorCount) + "次机会");
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
        // 验证输入
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseResult.fail("用户名不能为空");
        }
        if (password.isEmpty()) {
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

        return ResponseResult.ok("密码已重置为默认密码：a123456");
    }
}