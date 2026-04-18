package com.example.handmademarket.repository;

import com.example.handmademarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserAccount(String userAccount);

    boolean existsByUserAccount(String userAccount);
}
