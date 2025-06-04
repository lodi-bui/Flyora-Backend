package org.example.flyora_backend.repository;

import java.util.Optional;

import org.example.flyora_backend.model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDTO, Long> {
    Optional<UserDTO> findByUsername(String username);    
} 

