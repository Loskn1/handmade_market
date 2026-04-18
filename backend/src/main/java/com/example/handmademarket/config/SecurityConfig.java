package com.example.handmademarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ===================== 新增！解决本次致命报错的核心Bean =====================
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Spring官方标准密码加密器，完美适配AuthServiceImpl的依赖需求
        return new BCryptPasswordEncoder();
    }
    // ===========================================================================

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 所有接口100%无条件全部放行，无需登录、无需token
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // 彻底关闭CSRF防护（前后端分离项目必须关闭）
                .csrf(csrf -> csrf.disable())
                // 彻底关闭所有登录校验、session限制
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session.disable());

        // 全局跨域配置，完全放行你前端localhost:3000所有请求
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // 跨域配置
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}