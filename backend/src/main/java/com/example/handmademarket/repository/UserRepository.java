package com.example.handmademarket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.handmademarket.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserAccount(String userAccount);

    boolean existsByUserAccount(String userAccount);
}
