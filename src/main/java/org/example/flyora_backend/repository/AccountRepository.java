package org.example.flyora_backend.repository;

import java.util.Optional;

import org.example.flyora_backend.model.Account; // Giả sử dùng package model (đồng nhất với main)
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    boolean existsByUsername(String username);

    Optional<Account> findByUsernameAndPassword(String username, String password);
}
