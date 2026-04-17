package com.example.handmademarket.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    public String generateToken(String username) {
        // TODO: implement JWT token generation
        return "";
    }

    public boolean validateToken(String token) {
        // TODO: implement JWT validation
        return false;
    }

    public String getUsernameFromToken(String token) {
        // TODO: extract username from token
        return null;
    }
}
