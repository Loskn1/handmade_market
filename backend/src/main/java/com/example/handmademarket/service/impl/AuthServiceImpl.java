package com.example.handmademarket.service.impl;

import com.example.handmademarket.dto.LoginRequest;
import com.example.handmademarket.dto.RegisterRequest;
import com.example.handmademarket.entity.User;
import com.example.handmademarket.repository.UserRepository;
import com.example.handmademarket.service.AuthService;
import com.example.handmademarket.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUserAccount(request.getUserAccount());
        if (userOpt.isEmpty()) {
            return ResponseResult.fail("用户不存在");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseResult.fail("密码错误");
        }

        return ResponseResult.ok("登录成功");
    }

    @Override
    public ResponseResult register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByUserAccount(request.getUserAccount());
        if (existingUser.isPresent()) {
            return ResponseResult.fail("用户已存在");
        }

        User user = new User();
        user.setUserAccount(request.getUserAccount());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setUserName(request.getUserName());
        user.setPhone(request.getPhone());

        userRepository.save(user);
        return ResponseResult.ok("注册成功");
    }
}
