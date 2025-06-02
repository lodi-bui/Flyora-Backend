package org.example.flyora_backend.repository;

import java.util.Optional;

import org.example.flyora_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);    
} 

